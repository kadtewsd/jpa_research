package com.example.jpaexample.domain.model

import jakarta.persistence.*
import jakarta.persistence.Entity
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

// Base の方に @MappedSuperclass は必須。
// ない場合は、 Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Entity 'com.example.jpaexample.domain.model.AppUser' has no identifier (every '@Entity' class must declare or inherit at least one '@Id' or '@EmbeddedId' property)
// なお、@Entity の場合は、base_entity というテーブルが必要になってしまう
// DB の事情が入り込んでいるが、レイヤスーパータイプとして利用する。
@MappedSuperclass
abstract class BaseEntity(
    @field:Id
    @field: UuidGenerator(style = UuidGenerator.Style.RANDOM)
    protected val id: UUID? = null
)

@Embeddable
//@JvmInline value class にすると Unable to make field private final byte java.lang.String.coder accessible: module java.base does not "opens java.lang" to unnamed module @55f3ddb1
class UserId private constructor(val userId: String) {
    constructor() : this(
        userId = UUID.randomUUID().toString()
    )

    override fun equals(other: Any?): Boolean {
        return if (other !is UserId) false
        else other.userId == userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}

/**
 * allOpen をしているので、引数なしのコンストラクタが必要ない?
 * private constructor の意味がなくなり、かつ、
 * copy を使っては永続化しても値更新ができないが、equals などのメソッドを自力実装するよりはマシ、ということで data class にする
 */
@Entity
data class AppUser private constructor(
    @field: Embedded
    val userId: UserId,
    private var name: String,
) : BaseEntity() {

    constructor(name: String) : this(
        userId = UserId(),
        name = name,
    )

    fun name(): String = name

    fun changeName(name: String): AppUser {
        // data class で copy で設計すると、Query did not return a unique result: 2 results were returned になる。
        // おそらく別インスタンスなので別レコードとして Hibernate が扱っている
//          return this.copy(
//              name = name
//          )
        this.name = name
        return this
    }
}

interface AppUserRepository {
    fun addOrUpdate(appUser: AppUser): AppUser
    fun get(userId: UserId): AppUser?

    fun findBy(userId: UserId): AppUser?
}

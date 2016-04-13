package models.tables

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.driver.MySQLDriver
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tEmail.schema ++ tUser.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tEmail
   *  @param id Database column Id SqlType(INT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default()
   *  @param token Database column token SqlType(VARCHAR), Length(255,true), Default()
   *  @param validate Database column validate SqlType(INT), Default(0)
   *  @param expiredTime Database column expired_time SqlType(BIGINT), Default(0)
   *  @param addTime Database column add_time SqlType(BIGINT), Default(0) */
  case class rEmail(id: Int, email: String = "", token: String = "", validate: Int = 0, expiredTime: Long = 0L, addTime: Long = 0L)
  /** GetResult implicit for fetching rEmail objects using plain SQL queries */
  implicit def GetResultrEmail(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rEmail] = GR{
    prs => import prs._
    rEmail.tupled((<<[Int], <<[String], <<[String], <<[Int], <<[Long], <<[Long]))
  }
  /** Table description of table email. Objects of this class serve as prototypes for rows in queries. */
  class tEmail(_tableTag: Tag) extends Table[rEmail](_tableTag, "email") {
    def * = (id, email, token, validate, expiredTime, addTime) <> (rEmail.tupled, rEmail.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(email), Rep.Some(token), Rep.Some(validate), Rep.Some(expiredTime), Rep.Some(addTime)).shaped.<>({r=>import r._; _1.map(_=> rEmail.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true), Default() */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true), O.Default(""))
    /** Database column token SqlType(VARCHAR), Length(255,true), Default() */
    val token: Rep[String] = column[String]("token", O.Length(255,varying=true), O.Default(""))
    /** Database column validate SqlType(INT), Default(0) */
    val validate: Rep[Int] = column[Int]("validate", O.Default(0))
    /** Database column expired_time SqlType(BIGINT), Default(0) */
    val expiredTime: Rep[Long] = column[Long]("expired_time", O.Default(0L))
    /** Database column add_time SqlType(BIGINT), Default(0) */
    val addTime: Rep[Long] = column[Long]("add_time", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tEmail */
  lazy val tEmail = new TableQuery(tag => new tEmail(tag))

  /** Entity class storing rows of table tUser
   *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default()
   *  @param headimg Database column headImg SqlType(VARCHAR), Length(255,true), Default()
   *  @param sex Database column sex SqlType(INT), Default(0)
   *  @param phone Database column phone SqlType(VARCHAR), Length(255,true), Default()
   *  @param birthday Database column birthday SqlType(VARCHAR), Length(255,true), Default()
   *  @param nickname Database column nickName SqlType(VARCHAR), Length(255,true), Default() */
  case class rUser(id: Long, email: String = "", headimg: String = "", sex: Int = 0, phone: String = "", birthday: String = "", nickname: String = "")
  /** GetResult implicit for fetching rUser objects using plain SQL queries */
  implicit def GetResultrUser(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rUser] = GR{
    prs => import prs._
    rUser.tupled((<<[Long], <<[String], <<[String], <<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class tUser(_tableTag: Tag) extends Table[rUser](_tableTag, "user") {
    def * = (id, email, headimg, sex, phone, birthday, nickname) <> (rUser.tupled, rUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(email), Rep.Some(headimg), Rep.Some(sex), Rep.Some(phone), Rep.Some(birthday), Rep.Some(nickname)).shaped.<>({r=>import r._; _1.map(_=> rUser.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true), Default() */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true), O.Default(""))
    /** Database column headImg SqlType(VARCHAR), Length(255,true), Default() */
    val headimg: Rep[String] = column[String]("headImg", O.Length(255,varying=true), O.Default(""))
    /** Database column sex SqlType(INT), Default(0) */
    val sex: Rep[Int] = column[Int]("sex", O.Default(0))
    /** Database column phone SqlType(VARCHAR), Length(255,true), Default() */
    val phone: Rep[String] = column[String]("phone", O.Length(255,varying=true), O.Default(""))
    /** Database column birthday SqlType(VARCHAR), Length(255,true), Default() */
    val birthday: Rep[String] = column[String]("birthday", O.Length(255,varying=true), O.Default(""))
    /** Database column nickName SqlType(VARCHAR), Length(255,true), Default() */
    val nickname: Rep[String] = column[String]("nickName", O.Length(255,varying=true), O.Default(""))
  }
  /** Collection-like TableQuery object for table tUser */
  lazy val tUser = new TableQuery(tag => new tUser(tag))
}

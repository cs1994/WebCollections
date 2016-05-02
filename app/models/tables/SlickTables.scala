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
  lazy val schema: profile.SchemaDescription = Array(tComment.schema, tEmail.schema, tLabel.schema, tSave.schema, tTask.schema, tUser.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tComment
    *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param fromId Database column from_id SqlType(BIGINT), Default(0)
    *  @param toId Database column to_id SqlType(BIGINT), Default(0)
    *  @param content Database column content SqlType(VARCHAR), Length(255,true), Default()
    *  @param state Database column state SqlType(INT), Default(0)
    *  @param saveId Database column save_id SqlType(BIGINT), Default(0) */
  case class rComment(id: Long, fromId: Long = 0L, toId: Long = 0L, content: String = "", state: Int = 0, saveId: Long = 0L)
  /** GetResult implicit for fetching rComment objects using plain SQL queries */
  implicit def GetResultrComment(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rComment] = GR{
    prs => import prs._
      rComment.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[Int], <<[Long]))
  }
  /** Table description of table comment. Objects of this class serve as prototypes for rows in queries. */
  class tComment(_tableTag: Tag) extends Table[rComment](_tableTag, "comment") {
    def * = (id, fromId, toId, content, state, saveId) <> (rComment.tupled, rComment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(fromId), Rep.Some(toId), Rep.Some(content), Rep.Some(state), Rep.Some(saveId)).shaped.<>({r=>import r._; _1.map(_=> rComment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column from_id SqlType(BIGINT), Default(0) */
    val fromId: Rep[Long] = column[Long]("from_id", O.Default(0L))
    /** Database column to_id SqlType(BIGINT), Default(0) */
    val toId: Rep[Long] = column[Long]("to_id", O.Default(0L))
    /** Database column content SqlType(VARCHAR), Length(255,true), Default() */
    val content: Rep[String] = column[String]("content", O.Length(255,varying=true), O.Default(""))
    /** Database column state SqlType(INT), Default(0) */
    val state: Rep[Int] = column[Int]("state", O.Default(0))
    /** Database column save_id SqlType(BIGINT), Default(0) */
    val saveId: Rep[Long] = column[Long]("save_id", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tComment */
  lazy val tComment = new TableQuery(tag => new tComment(tag))

  /** Entity class storing rows of table tEmail
    *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default()
    *  @param token Database column token SqlType(VARCHAR), Length(255,true), Default()
    *  @param validate Database column validate SqlType(INT), Default(0)
    *  @param expiredTime Database column expired_time SqlType(BIGINT), Default(0)
    *  @param addTime Database column add_time SqlType(BIGINT), Default(0) */
  case class rEmail(id: Long, email: String = "", token: String = "", validate: Int = 0, expiredTime: Long = 0L, addTime: Long = 0L)
  /** GetResult implicit for fetching rEmail objects using plain SQL queries */
  implicit def GetResultrEmail(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rEmail] = GR{
    prs => import prs._
      rEmail.tupled((<<[Long], <<[String], <<[String], <<[Int], <<[Long], <<[Long]))
  }
  /** Table description of table email. Objects of this class serve as prototypes for rows in queries. */
  class tEmail(_tableTag: Tag) extends Table[rEmail](_tableTag, "email") {
    def * = (id, email, token, validate, expiredTime, addTime) <> (rEmail.tupled, rEmail.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(email), Rep.Some(token), Rep.Some(validate), Rep.Some(expiredTime), Rep.Some(addTime)).shaped.<>({r=>import r._; _1.map(_=> rEmail.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
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

  /** Entity class storing rows of table tLabel
    *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param taskId Database column task_id SqlType(BIGINT), Default(0)
    *  @param labelNum Database column label_num SqlType(INT), Default(0)
    *  @param userId Database column user_id SqlType(BIGINT), Default(0) */
  case class rLabel(id: Long, taskId: Long = 0L, labelNum: Int = 0, userId: Long = 0L)
  /** GetResult implicit for fetching rLabel objects using plain SQL queries */
  implicit def GetResultrLabel(implicit e0: GR[Long], e1: GR[Int]): GR[rLabel] = GR{
    prs => import prs._
      rLabel.tupled((<<[Long], <<[Long], <<[Int], <<[Long]))
  }
  /** Table description of table label. Objects of this class serve as prototypes for rows in queries. */
  class tLabel(_tableTag: Tag) extends Table[rLabel](_tableTag, "label") {
    def * = (id, taskId, labelNum, userId) <> (rLabel.tupled, rLabel.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(taskId), Rep.Some(labelNum), Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> rLabel.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column task_id SqlType(BIGINT), Default(0) */
    val taskId: Rep[Long] = column[Long]("task_id", O.Default(0L))
    /** Database column label_num SqlType(INT), Default(0) */
    val labelNum: Rep[Int] = column[Int]("label_num", O.Default(0))
    /** Database column user_id SqlType(BIGINT), Default(0) */
    val userId: Rep[Long] = column[Long]("user_id", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tLabel */
  lazy val tLabel = new TableQuery(tag => new tLabel(tag))

  /** Entity class storing rows of table tSave
    *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param url Database column url SqlType(VARCHAR), Length(255,true), Default()
    *  @param userId Database column user_id SqlType(BIGINT), Default(0)
    *  @param description Database column description SqlType(VARCHAR), Length(255,true), Default()
    *  @param secret Database column secret SqlType(INT), Default(0)
    *  @param number Database column number SqlType(INT), Default(0)
    *  @param webcontent Database column webcontent SqlType(VARCHAR), Length(1000,true), Default()
    *  @param insertTime Database column insert_time SqlType(BIGINT), Default(0)
    *  @param commentNum Database column comment_num SqlType(INT), Default(0)
    *  @param flag Database column flag SqlType(INT), Default(0) */
  case class rSave(id: Long, url: String = "", userId: Long = 0L, description: String = "", secret: Int = 0, number: Int = 0, webcontent: String = "", insertTime: Long = 0L, commentNum: Int = 0, flag: Int = 0)
  /** GetResult implicit for fetching rSave objects using plain SQL queries */
  implicit def GetResultrSave(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rSave] = GR{
    prs => import prs._
      rSave.tupled((<<[Long], <<[String], <<[Long], <<[String], <<[Int], <<[Int], <<[String], <<[Long], <<[Int], <<[Int]))
  }
  /** Table description of table save. Objects of this class serve as prototypes for rows in queries. */
  class tSave(_tableTag: Tag) extends Table[rSave](_tableTag, "save") {
    def * = (id, url, userId, description, secret, number, webcontent, insertTime, commentNum, flag) <> (rSave.tupled, rSave.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(url), Rep.Some(userId), Rep.Some(description), Rep.Some(secret), Rep.Some(number), Rep.Some(webcontent), Rep.Some(insertTime), Rep.Some(commentNum), Rep.Some(flag)).shaped.<>({r=>import r._; _1.map(_=> rSave.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column url SqlType(VARCHAR), Length(255,true), Default() */
    val url: Rep[String] = column[String]("url", O.Length(255,varying=true), O.Default(""))
    /** Database column user_id SqlType(BIGINT), Default(0) */
    val userId: Rep[Long] = column[Long]("user_id", O.Default(0L))
    /** Database column description SqlType(VARCHAR), Length(255,true), Default() */
    val description: Rep[String] = column[String]("description", O.Length(255,varying=true), O.Default(""))
    /** Database column secret SqlType(INT), Default(0) */
    val secret: Rep[Int] = column[Int]("secret", O.Default(0))
    /** Database column number SqlType(INT), Default(0) */
    val number: Rep[Int] = column[Int]("number", O.Default(0))
    /** Database column webcontent SqlType(VARCHAR), Length(1000,true), Default() */
    val webcontent: Rep[String] = column[String]("webcontent", O.Length(1000,varying=true), O.Default(""))
    /** Database column insert_time SqlType(BIGINT), Default(0) */
    val insertTime: Rep[Long] = column[Long]("insert_time", O.Default(0L))
    /** Database column comment_num SqlType(INT), Default(0) */
    val commentNum: Rep[Int] = column[Int]("comment_num", O.Default(0))
    /** Database column flag SqlType(INT), Default(0) */
    val flag: Rep[Int] = column[Int]("flag", O.Default(0))
  }
  /** Collection-like TableQuery object for table tSave */
  lazy val tSave = new TableQuery(tag => new tSave(tag))

  /** Entity class storing rows of table tTask
    *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param content Database column content SqlType(VARCHAR), Length(255,true), Default()
    *  @param state Database column state SqlType(INT), Default(0)
    *  @param insertTime Database column insert_time SqlType(BIGINT), Default(0) */
  case class rTask(id: Long, content: String = "", state: Int = 0, insertTime: Long = 0L)
  /** GetResult implicit for fetching rTask objects using plain SQL queries */
  implicit def GetResultrTask(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rTask] = GR{
    prs => import prs._
      rTask.tupled((<<[Long], <<[String], <<[Int], <<[Long]))
  }
  /** Table description of table task. Objects of this class serve as prototypes for rows in queries. */
  class tTask(_tableTag: Tag) extends Table[rTask](_tableTag, "task") {
    def * = (id, content, state, insertTime) <> (rTask.tupled, rTask.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(content), Rep.Some(state), Rep.Some(insertTime)).shaped.<>({r=>import r._; _1.map(_=> rTask.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column content SqlType(VARCHAR), Length(255,true), Default() */
    val content: Rep[String] = column[String]("content", O.Length(255,varying=true), O.Default(""))
    /** Database column state SqlType(INT), Default(0) */
    val state: Rep[Int] = column[Int]("state", O.Default(0))
    /** Database column insert_time SqlType(BIGINT), Default(0) */
    val insertTime: Rep[Long] = column[Long]("insert_time", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tTask */
  lazy val tTask = new TableQuery(tag => new tTask(tag))

  /** Entity class storing rows of table tUser
    *  @param id Database column Id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default()
    *  @param headImg Database column head_img SqlType(VARCHAR), Length(255,true), Default()
    *  @param sex Database column sex SqlType(INT), Default(0)
    *  @param phone Database column phone SqlType(VARCHAR), Length(255,true), Default()
    *  @param birthday Database column birthday SqlType(VARCHAR), Length(255,true), Default()
    *  @param nickName Database column nick_name SqlType(VARCHAR), Length(255,true), Default()
    *  @param secure Database column secure SqlType(VARCHAR), Length(255,true), Default()
    *  @param insertTime Database column insert_time SqlType(BIGINT), Default(0)
    *  @param ip Database column ip SqlType(VARCHAR), Length(255,true), Default()
    *  @param commentNum Database column comment_num SqlType(INT), Default(0) */
  case class rUser(id: Long, email: String = "", headImg: String = "", sex: Int = 0, phone: String = "", birthday: String = "", nickName: String = "", secure: String = "", insertTime: Long = 0L, ip: String = "", commentNum: Int = 0)
  /** GetResult implicit for fetching rUser objects using plain SQL queries */
  implicit def GetResultrUser(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rUser] = GR{
    prs => import prs._
      rUser.tupled((<<[Long], <<[String], <<[String], <<[Int], <<[String], <<[String], <<[String], <<[String], <<[Long], <<[String], <<[Int]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class tUser(_tableTag: Tag) extends Table[rUser](_tableTag, "user") {
    def * = (id, email, headImg, sex, phone, birthday, nickName, secure, insertTime, ip, commentNum) <> (rUser.tupled, rUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(email), Rep.Some(headImg), Rep.Some(sex), Rep.Some(phone), Rep.Some(birthday), Rep.Some(nickName), Rep.Some(secure), Rep.Some(insertTime), Rep.Some(ip), Rep.Some(commentNum)).shaped.<>({r=>import r._; _1.map(_=> rUser.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("Id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true), Default() */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true), O.Default(""))
    /** Database column head_img SqlType(VARCHAR), Length(255,true), Default() */
    val headImg: Rep[String] = column[String]("head_img", O.Length(255,varying=true), O.Default(""))
    /** Database column sex SqlType(INT), Default(0) */
    val sex: Rep[Int] = column[Int]("sex", O.Default(0))
    /** Database column phone SqlType(VARCHAR), Length(255,true), Default() */
    val phone: Rep[String] = column[String]("phone", O.Length(255,varying=true), O.Default(""))
    /** Database column birthday SqlType(VARCHAR), Length(255,true), Default() */
    val birthday: Rep[String] = column[String]("birthday", O.Length(255,varying=true), O.Default(""))
    /** Database column nick_name SqlType(VARCHAR), Length(255,true), Default() */
    val nickName: Rep[String] = column[String]("nick_name", O.Length(255,varying=true), O.Default(""))
    /** Database column secure SqlType(VARCHAR), Length(255,true), Default() */
    val secure: Rep[String] = column[String]("secure", O.Length(255,varying=true), O.Default(""))
    /** Database column insert_time SqlType(BIGINT), Default(0) */
    val insertTime: Rep[Long] = column[Long]("insert_time", O.Default(0L))
    /** Database column ip SqlType(VARCHAR), Length(255,true), Default() */
    val ip: Rep[String] = column[String]("ip", O.Length(255,varying=true), O.Default(""))
    /** Database column comment_num SqlType(INT), Default(0) */
    val commentNum: Rep[Int] = column[Int]("comment_num", O.Default(0))
  }
  /** Collection-like TableQuery object for table tUser */
  lazy val tUser = new TableQuery(tag => new tUser(tag))
}

import sbt._

object MicroServiceBuild extends Build with MicroService {

  val appName = "off-payroll-decision"

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.core.PlayVersion
  import play.sbt.PlayImport._

  private val microserviceBootstrapVersion = "10.0.0"
  private val domainVersion = "5.3.0"
  private val hmrcTestVersion = "3.3.0"
  private val scalaTestVersion = "3.0.3"
  private val pegdownVersion = "1.6.0"

  private val catsVersion = "0.8.0"

  val jsonValidationDependencies = Seq(
    "com.github.fge" % "json-schema-validator" % "2.2.6")

  val compile = Seq(
    "uk.gov.hmrc" %% "play-reactivemongo" % "6.2.0",
    "uk.gov.hmrc" %% "http-caching-client" % "8.0.0",
    "org.reactivemongo" %% "reactivemongo-akkastream" % "0.12.0",
    "ai.x" %% "play-json-extensions" % "0.8.0",
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % microserviceBootstrapVersion,
    "uk.gov.hmrc" %% "domain" % domainVersion,
    "org.typelevel" %% "cats" % catsVersion
  ) ++ jsonValidationDependencies

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
        "org.pegdown" % "pegdown" % pegdownVersion % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "uk.gov.hmrc" %% "play-reactivemongo" % "6.2.0" % scope,
        "uk.gov.hmrc" %% "http-caching-client" % "8.0.0 % scope",
        "org.mockito" % "mockito-core" % "2.23.0" % scope,
        "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % scope
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
        "org.pegdown" % "pegdown" % pegdownVersion % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}


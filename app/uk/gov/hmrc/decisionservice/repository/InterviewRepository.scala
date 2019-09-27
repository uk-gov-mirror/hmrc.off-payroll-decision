/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.decisionservice.repository

import javax.inject.{Inject, Singleton}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.Logger
import play.api.libs.json.Writes.StringWrites
import play.api.libs.json.{JsValue, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.DefaultDB
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson._
import reactivemongo.play.json.ImplicitBSONHandlers._
import uk.gov.hmrc.decisionservice.models.AnalyticsSearch._
import uk.gov.hmrc.decisionservice.models.{AnalyticsSearch, LogInterview}
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.mongo.ReactiveRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatedCacheMap(id: String,
                         data: Map[String, JsValue],
                         lastUpdated: DateTime = DateTime.now(DateTimeZone.UTC))

object DatedCacheMap {

  implicit val formats = Json.format[DatedCacheMap]

  def apply(cacheMap: CacheMap): DatedCacheMap = DatedCacheMap(cacheMap.id, cacheMap.data)
}


class ReactiveMongoRepository(mongo: () => DefaultDB)
  extends ReactiveRepository[DatedCacheMap, BSONObjectID]("Off-Payroll-Interview", mongo, DatedCacheMap.formats) {

  def save(i: LogInterview) : Future[WriteResult] = collection.insert(i)

  def count(search: AnalyticsSearch): Future[Int] = {
    val query = Json.obj("decision" -> search.decision,  "completed" ->
      Json.obj("$gte" -> search.start, "$lt" -> search.end))
    Logger.info(s"[InterviewRepository][count] $query")
    collection.count(Some(query))
  }

  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def read(time: BSONDateTime) = new DateTime(time.value)
    def write(time: DateTime) = BSONDateTime(time.getMillis)
  }
}

@Singleton
class InterviewRepository @Inject()(mongo: ReactiveMongoComponent) {

  private lazy val interviewRepository = new ReactiveMongoRepository(mongo.mongoConnector.db)

  def apply(): ReactiveMongoRepository = interviewRepository
}

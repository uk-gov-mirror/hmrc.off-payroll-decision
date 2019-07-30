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

package uk.gov.hmrc.decisionservice.services

import uk.gov.hmrc.decisionservice.models.BusinessOnOwnAccount
import uk.gov.hmrc.play.test.UnitSpec

class BusinessOnOwnAccountDecisionServiceSpec extends UnitSpec {

  object TestBusinessOnOwnAccountDecisionService extends BusinessOnOwnAccountDecisionService

  "BusinessOnOwnAccountDecisionService" when {

    "decide is called" should {

      "return a None" in {

        val actualResult = TestBusinessOnOwnAccountDecisionService.decide(BusinessOnOwnAccount(None, None, None, None, None))
        val expectedResult = None

        await(actualResult) shouldBe expectedResult
      }
    }
  }
}

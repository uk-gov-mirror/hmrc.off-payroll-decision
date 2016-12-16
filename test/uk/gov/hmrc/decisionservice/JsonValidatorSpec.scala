/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.decisionservice

import cats.data.Xor
import uk.gov.hmrc.decisionservice.util.JsonValidator.validate
import uk.gov.hmrc.play.test.UnitSpec


class JsonValidatorSpec extends UnitSpec {

  val valid_twoSections = """{
                                  "version": "89.90.73C",
                                  "correlationID": "adipisicing ullamco",
                                  "interview" : {
                                  "personalService": {
                                    "workerSentActualSubstitiute": "false",
                                    "engagerArrangeWorker": "false",
                                    "contractualRightForSubstitute": "false",
                                    "workerPayActualHelper": "false",
                                    "workerSentActualHelper": "true",
                                    "contractrualObligationForSubstitute": "false",
                                    "contractTermsWorkerPaysSubstitute": "false"
                                  },
                                  "partOfOrganisation": {
                                    "workerAsLineManager": "false",
                                    "workerRepresentsEngagerBusiness": "false",
                                    "contactWithEngagerCustomer": "false"
                                  }}
                                }"""

  val valid_noAnswers = """{
                              "version": "89.90.73C",
                              "correlationID": "adipisicing ullamco",
                              "interview": {
                              "personalService": {}
                              }
                            }"""

  val invalid_missingCorrelationID = """{
                                  "version": "89.90.73C",
                                  "personalService": {
                                    "workerSentActualSubstitiute": "false",
                                    "engagerArrangeWorker": "false",
                                    "contractualRightForSubstitute": "false",
                                    "workerPayActualHelper": "false",
                                    "workerSentActualHelper": "true",
                                    "contractrualObligationForSubstitute": "false",
                                    "contractTermsWorkerPaysSubstitute": "false"
                                  },
                                  "partOfOrganisation": {
                                    "workerAsLineManager": "false",
                                    "workerRepresentsEngagerBusiness": "false",
                                    "contactWithEngagerCustomer": "false"
                                  },
                                  "miscellaneous": {},
                                  "businessStructure": {}
                                }"""

  val invalid_missingVersion = """{
                                  "correlationID": "adipisicing ullamco",
                                  "personalService": {
                                    "workerSentActualSubstitiute": "false",
                                    "engagerArrangeWorker": "false",
                                    "contractualRightForSubstitute": "false",
                                    "workerPayActualHelper": "false",
                                    "workerSentActualHelper": "true",
                                    "contractrualObligationForSubstitute": "false",
                                    "contractTermsWorkerPaysSubstitute": "false"
                                  },
                                  "partOfOrganisation": {
                                    "workerAsLineManager": "false",
                                    "workerRepresentsEngagerBusiness": "false",
                                    "contactWithEngagerCustomer": "false"
                                  },
                                  "miscellaneous": {},
                                  "businessStructure": {}
                                }"""

  val invalid_invalidAnswerValue = """{
                                  "version": "89.90.73C",
                                  "correlationID": "adipisicing ullamco",
                                  "interview" : {
                                    "personalService": {
                                      "workerSentActualSubstitiute": "false",
                                      "engagerArrangeWorker": "false",
                                      "contractualRightForSubstitute": "false",
                                      "workerPayActualHelper": "false",
                                      "workerSentActualHelper": "true",
                                      "contractrualObligationForSubstitute": "false",
                                      "contractTermsWorkerPaysSubstitute": "false"
                                    },
                                    "partOfOrganisation": {
                                      "workerAsLineManager": true,
                                      "workerRepresentsEngagerBusiness": "false",
                                      "contactWithEngagerCustomer": "false"
                                    },
                                    "miscellaneous": {},
                                    "businessStructure": {}
                                  }
                                }"""

  val invalid_invalidSection = """{
                                  "version": "89.90.73C",
                                  "correlationID": "adipisicing ullamco",
                                  "interview": {
                                    "personalService": {
                                      "workerSentActualSubstitiute": "false",
                                      "engagerArrangeWorker": "false",
                                      "contractualRightForSubstitute": "false",
                                      "workerPayActualHelper": "false",
                                      "workerSentActualHelper": "true",
                                      "contractrualObligationForSubstitute": "false",
                                      "contractTermsWorkerPaysSubstitute": "false"
                                    },
                                    "invalidSection": {
                                      "invalidQuestion1": "false",
                                      "invalidQuestion2": "false",
                                      "invalidQuestion3": "true"
                                    },
                                    "miscellaneous": {},
                                    "businessStructure": {}
                                  }
                                }"""


  val invalid_invalidEnum = """{
                                   "version": "78.8.18Q",
                                   "correlationID": "dolor quis cillum velit in",
                                   "interview": {
                                     "personalService": {
                                       "contractrualObligationForSubstitute": "false",
                                       "workerSentActualSubstitiute": "false",
                                       "workerSentActualHelper": "true",
                                       "engagerArrangeWorker": "true",
                                       "possibleHelper": "true"
                                     },
                                     "partOfOrganisation": {
                                       "contactWithEngagerCustomer": "true",
                                       "workerReceivesBenefits": "false",
                                       "workerAsLineManager": "false"
                                     },
                                     "businessStructure": {
                                       "businessWebsite": "true",
                                       "businesAccount": "false",
                                       "workerPayForTraining": "false"
                                     },
                                     "control": {
                                       "workerDecideWhere": "workerDecideWhere",
                                       "workerLevelOfExpertise": "imWellGood"
                                     },
                                     "miscellaneous": {}
                                   }
                                 }"""

  val invalid_invalidEnum2 = """{
                                     "version": "5.4.2-b",
                                     "correlationID": "dolor dolor",
                                     "interview": {
                                       "personalService": {
                                         "workerSentActualHelper": "false",
                                         "workerSentActualSubstitiute": "false"
                                       },
                                       "financialRisk": {
                                         "workerProvideConsumablesMaterials": "true",
                                         "engagerPayExpense": "false",
                                         "workerMainIncome": "allDayEveryDay"
                                       }
                                     }
                                   }"""


  "json validator" should {

    "return true for valid json" in {
      validate(valid_twoSections).isRight shouldBe true
    }

    "return true for valid json - no answers" in {
      validate(valid_noAnswers).isRight shouldBe true
    }

    "return false for invalid json - InvalidAnswerValue" in {
      verify(invalid_invalidAnswerValue, "string")
    }

    "return false for invalid json - missing Version" in {
      verify(invalid_missingVersion, "object has missing required properties")
    }

    "return false for invalid json - missing CorrelationID" in {
      verify(invalid_missingCorrelationID, "object has missing required properties")
    }

    "return false for invalid json - invalidSection" in {
      verify(invalid_invalidSection, "[\"invalidSection\"]")
    }

    "return true for valid json - empty interview" in {
      val valid_emptyInterview =
        """{
             "version": "15.16.1-S",
             "correlationID": "ut",
             "interview" : {}
        }"""
      validate(valid_emptyInterview).isRight shouldBe true
    }

    "return false for invalid json - invalidFormatVersionId - should be string" in {
      val invalid_versionIdType =
        """{
        "version": 342571,
        "correlationID": "ut",
        "interview" : {}
      }"""
      verify(invalid_versionIdType, "integer")
    }

    "return false for invalid json - invalidFormatVersionId" in {
      val invalid_versionId =
        """{
        "version": "001-SNAPSHOT",
        "correlationID": "ut",
        "interview" : {}
      }"""
      verify(invalid_versionId, "001-SNAPSHOT")
    }

    "return true for valid json - valid version id" in {
      val valid_versionId =
        """{
        "version": "0.0.1-alpha",
        "correlationID": "ut",
        "interview" : {}
      }"""
      validate(valid_versionId).isRight shouldBe true
    }

    "return false for invalid json - enum value is not valid" in {
      verify(invalid_invalidEnum, "instance value (\"imWellGood\") not found in enum")
    }

    "return false for invalid json - enum value is not valid2" in {
      verify(invalid_invalidEnum2, "instance value (\"allDayEveryDay\") not found in enum")
    }

  }

  val verify = verifyError(validate) _

  def verifyError(f:String => Xor[String,Unit])(s:String, expectedText:String):Unit = {
    val result = validate(s)
    println(result)
    result.isRight shouldBe false
    result.leftMap { report =>
      report.contains(expectedText) shouldBe true
    }
  }

}

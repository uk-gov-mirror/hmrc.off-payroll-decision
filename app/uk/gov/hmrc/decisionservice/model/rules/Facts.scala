package uk.gov.hmrc.decisionservice.model.rules

import cats.data.Xor
import uk.gov.hmrc.decisionservice.model.DecisionServiceError
import uk.gov.hmrc.decisionservice.ruleengine.FactMatcherInstance
import play.api.Logger

case class Facts(facts:Map[String,CarryOver]){

  def ==+>:(rules:SectionRuleSet):Xor[DecisionServiceError,Facts] = {
    val defaultFactName = rules.section
    Logger.info(s"matching for section:\t'$defaultFactName'")
    Logger.info(s"headings:\t${rules.headings.mkString("\t,")}")
    Logger.info(s"facts:   \t${rules.headings.map(facts.getOrElse(_, >>>("")).value).mkString("\t,")}")
    FactMatcherInstance.matchFacts(facts,rules).map { carryOver =>
      val factName = carryOver.name.getOrElse(defaultFactName)
      val newFact = (factName -> carryOver)
      Logger.info(s"new fact:\t$factName -> '${carryOver.value}' ${if (carryOver.exit) "EXIT" else ""}")
      Facts(facts + newFact)
    }
  }

}


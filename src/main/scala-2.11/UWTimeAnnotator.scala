package demo.uwtime.nikhil

import edu.uw.cs.lil.uwtime.api.{DocumentAnnotation, SentenceAnnotation, MentionAnnotation, TemporalAnnotator}
import edu.uw.cs.lil.uwtime.learn.temporal.MentionResult
import edu.uw.cs.lil.uwtime.utils.TemporalDomain
import scala.collection.JavaConversions._

case class DocumentCreationDate(date: String)

case class DocumentText(text:String)

case class UWTimeDocument(text: String)

case class Email(messageId: String, cleanedText: String, receivedDate: String)

object UWTimeAnnotator {

  //TODO:  Connect I/O operations from Database to replace hard coded fixture
  val mockEmails: List[Email] = List(
    Email(
      "<abc@mail.gmail.com>",
      "Hello, 9:15 a.m. PST works for me",
      "2016-05-13"
    )
  )

  val temporalAnnotator = new TemporalAnnotator()
  val domain = TemporalDomain.OTHER

  def run () = {
    mockEmails.foreach(annotateEmail)
  }

  def extractMentionAnnotations(sentenceAnnotations: Seq[SentenceAnnotation]) = {
    sentenceAnnotations
      .flatMap(_
        .getMentionAnnotations)
  }

  def extractTimeEntityResults(mentionAnnotations: Seq[MentionAnnotation]) = {
    mentionAnnotations
      .map(
        _.getMention)
      .map(
        _.getResult)
  }

  def extractTimeEntityTypes (timeEntityResults:Seq[MentionResult]):Seq[String] = {
    timeEntityResults
      .map(
        _.getType)
  }


  def extractTimeEntityValues (timeEntityResults: Seq[MentionResult]):Seq[String] = {
    timeEntityResults
      .map(
        _.getValue)
  }

  def printReport(email:Email, timeEntityTypes: Seq[String], timeEntityValues:Seq[String]):Unit = {
    println ("MessageId: "+email.messageId)
    println ("Cleaned Text: "+email.cleanedText)
    println ("Received Date: "+email.receivedDate)
    println("The time entity types are: " + timeEntityTypes)
    println("The time entity values are: " + timeEntityValues)

  }

  def annotateEmail (email:Email) = {

    val documentText: DocumentText = DocumentText(email.cleanedText)
    val documentCreationDate: DocumentCreationDate = DocumentCreationDate(email.receivedDate)
    val documentAnnotation: DocumentAnnotation = temporalAnnotator.extract(documentText.text, documentCreationDate.date, domain )
    val sentenceAnnotations: Seq[SentenceAnnotation] = documentAnnotation.getSentenceAnnotations.toSeq

    val timeEntityResults: Seq[MentionResult] = extractTimeEntityResults(extractMentionAnnotations(sentenceAnnotations))

    val timeEntityTypes: Seq[String] = extractTimeEntityTypes(timeEntityResults)
    val timeEntityValues: Seq[String] = extractTimeEntityValues(timeEntityResults)

    printReport(email, timeEntityTypes, timeEntityValues)

  }

}
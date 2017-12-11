package gcombrinck.lib

import java.util
import java.util.regex.Pattern
import javax.mail.internet.MimeMultipart
import javax.mail.search.FlagTerm
import javax.mail.{Flags, Folder, Message, MessagingException, NoSuchProviderException, Session}



object MailHelper {
  private val EMAIL = "regression@healthqtech.com"
  private val PASSWORD = "Healthq123456"

  private def extractUrls(text: String): util.ArrayList[String] = {
    val containedUrls = new util.ArrayList[String]
    val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
    val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
    val urlMatcher = pattern.matcher(text)
    while ( {
      urlMatcher.find
    })
      containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
    val it = containedUrls.iterator()
    while (it.hasNext){
       val obj = it.next()
      if(!obj.contains("https://userportal.testing.gcombrinck.co/activate")){
        it.remove()
      }
    }
    containedUrls
  }

  def verifyUserRegistration(): Any = {
    val props = System.getProperties
    props.setProperty("mail.store.protocol", "imaps")
    val session = Session.getDefaultInstance(props, null)
    val store = session.getStore("imaps")
    try {
      store.connect("imap.gmail.com", EMAIL, PASSWORD)
      val inbox = store.getFolder("Inbox")
      inbox.open(Folder.READ_WRITE)

      val messages: Array[Message] = inbox.search(
        new FlagTerm(new Flags(Flags.Flag.SEEN), false))
      var content = ""
      for (message <- messages) {
        if (message.getSubject.contains("Activate your profile")) {
          if (message.getContent.isInstanceOf[String]) {
            val multipart = message.getContent
            content = multipart.toString
          } else {
            val multipart = message.getContent.asInstanceOf[MimeMultipart]
            content = multipart.getBodyPart(1).getContent.toString
          }
          val url = extractUrls(content)
          val os = System.getProperty("os.name").toLowerCase()
          val rt = Runtime.getRuntime
          if (os.indexOf("mac") >= 0) {
            rt.exec("open " + url.get(0))
          }
          if (os.indexOf("windows") >= 0) {
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url.get(0))
          }
          if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            rt.exec("/usr/bin/google-chrome --headless " + url.get(0))
          }
          message.setFlag(Flags.Flag.SEEN, true)
        }
      }
      inbox.close(true)
    } catch {
      case e: NoSuchProviderException => e.printStackTrace()
        System.exit(1)
      case me: MessagingException => me.printStackTrace()
        System.exit(2)
    } finally {
      store.close()
    }
  }


  /*For Mobile Tests*/
  def getRegistrationURLS: util.ArrayList[String] = {
    val props = System.getProperties
    var url = new util.ArrayList[String]
    props.setProperty("mail.store.protocol", "imaps")
    val session = Session.getDefaultInstance(props, null)
    val store = session.getStore("imaps")
    try {
      store.connect("imap.gmail.com", EMAIL, PASSWORD)
      val inbox = store.getFolder("Inbox")
      inbox.open(Folder.READ_WRITE)

      val messages: Array[Message] = inbox.search(
        new FlagTerm(new Flags(Flags.Flag.SEEN), false))
      var content = ""
      for (message <- messages) {
        if (message.getSubject.contains("Activate your profile")) {
          if (message.getContent.isInstanceOf[String]) {
            val multipart = message.getContent
            content = multipart.toString
          } else {
            val multipart = message.getContent.asInstanceOf[MimeMultipart]
            content = multipart.getBodyPart(1).getContent.toString
          }
          url = extractUrls(content)

        }
      }
      inbox.close(true)
    } catch {
      case e: NoSuchProviderException => e.printStackTrace()
        System.exit(1)
      case me: MessagingException => me.printStackTrace()
        System.exit(2)
    } finally {
      store.close()
    }
    url
  }

  def mark_all_emails_as_read(): Unit = {
    val props = System.getProperties
    props.setProperty("mail.store.protocol", "imaps")
    val session = Session.getDefaultInstance(props, null)
    val store = session.getStore("imaps")
    try {
      store.connect("imap.gmail.com", EMAIL, PASSWORD)
      val inbox = store.getFolder("Inbox")
      inbox.open(Folder.READ_WRITE)
      val messages: Array[Message] = inbox.search(
        new FlagTerm(new Flags(Flags.Flag.SEEN), false))
      for (message <- messages) {
        message.setFlag(Flags.Flag.SEEN, true)
      }

    } catch {
      case e: NoSuchProviderException => e.printStackTrace()
        System.exit(1)
      case me: MessagingException => me.printStackTrace()
        System.exit(2)
    } finally {
      store.close()
    }
  }
}

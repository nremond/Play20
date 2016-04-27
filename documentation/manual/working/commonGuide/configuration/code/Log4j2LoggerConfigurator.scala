import java.io.File
import java.net.{URI, URL}

/*
// #log4j2-import
import play.api.{Mode, Configuration, Environment, LoggerConfigurator}

import org.slf4j.ILoggerFactory

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core._
import org.apache.logging.log4j.core.config.Configurator
// #log4j2-import
*/

import play.api.{Mode, Configuration, Environment, LoggerConfigurator}
import org.slf4j.ILoggerFactory

//#log4j2-class
class Log4J2LoggerConfigurator extends LoggerConfigurator {

  private var factory: ILoggerFactory = _

  override def init(rootPath: File, mode: Mode.Mode): Unit = {
    val properties = Map("application.home" -> rootPath.getAbsolutePath)
    val resourceName = "log4j2.xml"
    val resourceUrl = Option(this.getClass.getClassLoader.getResource(resourceName))
    configure(properties, resourceUrl)
  }

  override def shutdown(): Unit = {
    val context = LogManager.getContext().asInstanceOf[LoggerContext]
    Configurator.shutdown(context)
  }

  override def configure(env: Environment): Unit = {
    val properties = LoggerConfigurator.generateProperties(env, Configuration.empty, Map.empty)
    val resourceUrl = env.resource("log4j2.xml")
    configure(properties, resourceUrl)
  }

  override def configure(env: Environment, configuration: Configuration, optionalProperties: Map[String, String]): Unit = {
    // LoggerConfigurator.generateProperties enables play.logger.includeConfigProperties=true
    val properties = LoggerConfigurator.generateProperties(env, configuration, optionalProperties)
    val resourceUrl = env.resource("log4j2.xml")
    configure(properties, resourceUrl)
  }

  override def configure(properties: Map[String, String], config: Option[URL]): Unit = {
    val context = LogManager.getContext(false).asInstanceOf[LoggerContext]
    context.setConfigLocation(config.get.toURI)

    factory = org.slf4j.impl.StaticLoggerBinder.getSingleton.getLoggerFactory
  }

  override def loggerFactory: ILoggerFactory = factory
}
//#log4j2-class

object Configurator {
  def shutdown(context: Any): Unit = ???

}

object LogManager {
  def getContext() = ???

  def getContext(b: Boolean) = ???

}

class LoggerContext {
  def setConfigLocation(toURI: URI): Unit = ???

}
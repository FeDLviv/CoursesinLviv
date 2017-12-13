package fed.spring;

import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fed.spring.beans.Client;
import fed.spring.beans.Event;
import fed.spring.beans.EventType;
import fed.spring.loggers.EventLogger;

public class App {

	private Client client;
	private EventLogger defaultLogger;
	private Map<EventType, EventLogger> loggers;
	private String startMsg;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		App app = (App) context.getBean("app");

		System.out.println(app.startMsg);

		Event event = context.getBean(Event.class);
		app.logEvent(EventType.INFO, event, "Some event for user 1");

		event = context.getBean(Event.class);
		app.logEvent(EventType.ERROR, event, "Some event for user 2 (error)");

		event = context.getBean(Event.class);
		app.logEvent(null, event, "Some event for user 3");

		context.close();
	}

	public App(Client client, EventLogger defaultLogger, Map<EventType, EventLogger> loggers) {
		this.client = client;
		this.defaultLogger = defaultLogger;
		this.loggers = loggers;
	}

	public void setStartMsg(String msg) {
		this.startMsg = msg;
	}

	public void logEvent(EventType type, Event event, String msg) {
		String message = msg.replaceAll(client.getId(), client.getFullName());
		event.setMsg(message);

		EventLogger logger = loggers.get(type);
		if (logger == null) {
			logger = defaultLogger;
		}

		logger.logEvent(event);
	}

}

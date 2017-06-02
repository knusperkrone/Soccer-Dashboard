import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class F24_Parser {

    static class Event {
        final String id;
        final List<EventPart> parts = new ArrayList<>();

        public Event(String id)
        {
            this.id = id;
        }
    }

    static class EventPart {
        final String period, min, sec;
        final String player_id, team_id;
        final String outcome;
        final String x, y;
        final List<Qualifier> qualifiers = new ArrayList<>();

        public EventPart(String period, String min, String sec, String player_id, String team_id, String outcome, String x, String y)
        {
            this.period = period;
            this.min = min;
            this.sec = sec;
            this.player_id = player_id;
            this.team_id = team_id;
            this.outcome = outcome;
            this.x = x;
            this.y = y;
        }
    }

    static class Qualifier {
        final String id, qualifier_id, value;

        public Qualifier(String id, String qualifier_id, String value)
        {
            this.id = id;
            this.qualifier_id = qualifier_id;
            this.value = value;
        }
    }


    private final Document doc;

    String gameId, name, homeTeamId, awayTeamId;
    final HashMap<String, Event> events = new HashMap<>();

    F24_Parser(String path) throws Exception
    {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        setGame();
        setEvents();
    }

    void setGame()
    {
        NamedNodeMap gameAttrs= doc.getElementsByTagName("Game").item(0).getAttributes();
        gameId = gameAttrs.getNamedItem("id").getNodeValue();
        name = gameAttrs.getNamedItem("competition_name").getNodeValue();
        homeTeamId = gameAttrs.getNamedItem("home_team_id").getNodeValue();
        awayTeamId = gameAttrs.getNamedItem("away_team_id").getNodeValue();
    }

    void setEvents()
    {
        NodeList events= doc.getElementsByTagName("Event");
        for (int i = 0; i < events.getLength(); i++) {
            addEvent(events.item(i));
        }
    }

    void addEvent(Node event) {
        NamedNodeMap eventAttrs = event.getAttributes();
        String eId = eventAttrs.getNamedItem("event_id").getNodeValue();
        if (!events.containsKey(eId))
            events.put(eId, new Event(eId));
        Event currEvent = events.get(eId);

        String period, min, sec;
        String player_id, team_id;
        String outcome;
        String x, y;

        period = eventAttrs.getNamedItem("period_id").getNodeValue();
        min = eventAttrs.getNamedItem("min").getNodeValue();
        sec =  eventAttrs.getNamedItem("sec").getNodeValue();
        if (eventAttrs.getNamedItem("player_id") != null)
            player_id = eventAttrs.getNamedItem("player_id").getNodeValue();
        else
            player_id = null;
        team_id = eventAttrs.getNamedItem("team_id").getNodeValue();
        outcome = eventAttrs.getNamedItem("outcome").getNodeValue();
        x = eventAttrs.getNamedItem("x").getNodeValue();
        y = eventAttrs.getNamedItem("y").getNodeValue();

        EventPart ePart = new EventPart(period, min, sec, player_id, team_id, outcome, x, y);
        addQualifiers(ePart, event.getChildNodes());
        currEvent.parts.add(ePart);
    }

    void addQualifiers(EventPart ePart, NodeList qs) {
        String id, qualifier_id, value;
        NamedNodeMap qAttrs;
        for (int i = 0; i < 0; i++) {
                qAttrs = qs.item(i).getAttributes();
                id = qAttrs.getNamedItem("id").getNodeValue();
                qualifier_id = qAttrs.getNamedItem("qulifier_id").getNodeValue();
                if (qAttrs.getNamedItem("value") != null) {
                    value = qAttrs.getNamedItem("value").getNodeValue();
                } else {
                    value = null;
                }
                ePart.qualifiers.add(new Qualifier(id, qualifier_id, value));
        }
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("GameId: "+ gameId + "\nHomeTeamId: "+ homeTeamId + "\nAwayTeamId: "+ awayTeamId +"\n");
        for (Event event : events.values()) {
            builder.append(event.toString());
            builder.append("\n");
        }
        return builder.toString();
    }


    public static void main(String[] args)
    {

        F24_Parser parsed;
        try {
            // TODO: Parse directory
            // TODO: DatabaseInsert
            String path = "tmp24.xml";

            parsed = new F24_Parser(path);
            System.out.println(parsed.toString());

        } catch (Exception e) {
            System.out.println(e);
        }
    }


}

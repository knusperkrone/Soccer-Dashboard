import DatabaseTables.DataPool;
import DatabaseTables.Table_Event;
import DatabaseTables.Table_EventData;
import DatabaseTables.Table_Qualifier;
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

    final HashMap<String, Event> events = new HashMap<>();
    private final Document doc;
    String gameId, name, homeTeamId, awayTeamId;

    public F24_Parser(String path) throws Exception
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
        NamedNodeMap gameAttrs = doc.getElementsByTagName("Game").item(0).getAttributes();
        gameId = gameAttrs.getNamedItem("id").getNodeValue();
        name = gameAttrs.getNamedItem("competition_name").getNodeValue();
        homeTeamId = gameAttrs.getNamedItem("home_team_id").getNodeValue();
        awayTeamId = gameAttrs.getNamedItem("away_team_id").getNodeValue();
    }

    void setEvents()
    {
        NodeList events = doc.getElementsByTagName("Event");
        for (int i = 0; i < events.getLength(); i++) {
            addEvent(events.item(i));
        }
    }

    void addEvent(Node event)
    {
        NamedNodeMap eventAttrs = event.getAttributes();
        String eId = eventAttrs.getNamedItem("event_id").getNodeValue();
        if (!events.containsKey(eId))
            events.put(eId, new Event(eId));
        Event currEvent = events.get(eId);

        String period, min, sec;
        String type_Id;
        String player_id, team_id;
        String outcome;
        String x, y;

        period = eventAttrs.getNamedItem("period_id").getNodeValue();
        min = eventAttrs.getNamedItem("min").getNodeValue();
        sec = eventAttrs.getNamedItem("sec").getNodeValue();
        type_Id = eventAttrs.getNamedItem("type_id").getNodeValue();
        if (eventAttrs.getNamedItem("player_id") != null)
            player_id = eventAttrs.getNamedItem("player_id").getNodeValue();
        else
            player_id = null;
        team_id = eventAttrs.getNamedItem("team_id").getNodeValue();
        outcome = eventAttrs.getNamedItem("outcome").getNodeValue();
        x = eventAttrs.getNamedItem("x").getNodeValue();
        y = eventAttrs.getNamedItem("y").getNodeValue();

        EventData ePart = new EventData(type_Id, period, min, sec, player_id, team_id, outcome, x, y);
        addQualifiers(ePart, event.getChildNodes());
        currEvent.parts.add(ePart);
    }

    void addQualifiers(EventData ePart, NodeList qs)
    {
        String id, qualifier_id, value;
        NamedNodeMap qAttrs;
        for (int i = 0; i < qs.getLength(); i++) {
            qAttrs = qs.item(i).getAttributes();
            if (qAttrs == null)
                continue;

            id = qAttrs.getNamedItem("id").getNodeValue();
            qualifier_id = qAttrs.getNamedItem("qualifier_id").getNodeValue();
            if (qAttrs.getNamedItem("value") != null) {
                value = qAttrs.getNamedItem("value").getNodeValue();
            } else {
                value = null;
            }
            ePart.qualifiers.add(new Qualifier(id, qualifier_id, value));
        }
    }

    public void addData(DataPool pool)
    {
        // All you need here is @gameId
        for (Event event : events.values()) {
            event.addData(pool, gameId);
        }
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("GameId: " + gameId + "\nHomeTeamId: " + homeTeamId + "\nAwayTeamId: " + awayTeamId + "\n");
        for (Event event : events.values()) {
            builder.append(event.toString());
            builder.append("\n");
        }
        return builder.toString();
    }


    static class Event {
        final String id;
        final List<EventData> parts = new ArrayList<>();

        Event(String id)
        {
            this.id = id;
        }

        void addData(DataPool pool, String gameId)
        {
            pool.events.add(new Table_Event(id, gameId));
            for (EventData part : parts) {
                part.addData(pool, gameId, id);
            }
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder("\tEvent_id: " + id + "\n");
            for (EventData part : parts) {
                builder.append(part.toString());
                builder.append("\n");
            }
            return builder.toString();
        }
    }

    static class EventData {
        final String period, min, sec;
        final String player_id, team_id;
        final String type_Id;
        final String outcome;
        final String x, y;
        final List<Qualifier> qualifiers = new ArrayList<>();

        EventData(String type_Id, String period, String min, String sec, String player_id, String team_id, String outcome, String x, String y)
        {
            this.type_Id = type_Id;
            this.period = period;
            this.min = min;
            this.sec = sec;
            this.player_id = player_id;
            this.team_id = team_id;
            this.outcome = outcome;
            this.x = x;
            this.y = y;
        }

        void addData(DataPool pool, String gameId, String eventId)
        {
            for (Qualifier qualifier : qualifiers) {
                pool.eventData.add(new Table_EventData(eventId, gameId, type_Id, period, min, sec, qualifier.id, player_id, team_id, outcome, x, y));
                qualifier.addData(pool);
            }
        }

        @Override
        public String toString()
        {
            String tab = "\t\t";
            StringBuilder builder = new StringBuilder(tab + "period: " + period + ", min: " + min + ", sec: " + sec + "\n"
                    + tab + "player_id: " + player_id + ", team_id: " + team_id + "\n"
                    + tab + "outcome: " + outcome + "\n"
                    + tab + "x: " + x + ", y: " + y + "\n");
            for (Qualifier quali : qualifiers) {
                builder.append(quali.toString());
                builder.append("\n");
            }
            return builder.toString();
        }
    }

    static class Qualifier {
        final String id, qualifier_id, value;

        Qualifier(String id, String qualifier_id, String value)
        {
            this.id = id;
            this.qualifier_id = qualifier_id;
            this.value = value;
        }

        void addData(DataPool pool)
        {
            pool.qualifiers.add(new Table_Qualifier(id, qualifier_id, value));
        }

        @Override
        public String toString()
        {
            return "\t\t\t" + "id: " + id + ", qualifier_id: " + qualifier_id + ", value: " + value;
        }
    }


}

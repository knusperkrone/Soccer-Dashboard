import DatabaseTables.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

public class F7_Parser {

    final F7_Team homeTeam = new F7_Team("Home");
    final F7_Team guestTeam = new F7_Team("Away");
    private final Document doc;
    String game_uID, first_half, second_half;

    F7_Parser(String path) throws Exception
    {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        set_uID();
        set_half_times();
        set_LineUp(homeTeam);
        set_LineUp(guestTeam);
    }

    private void set_uID()
    {
        game_uID = doc.getElementsByTagName("SoccerDocument").item(0).getAttributes().getNamedItem("uID").getTextContent();
    }

    private void set_half_times() throws Exception
    {
        NodeList stats = doc.getElementsByTagName("Stat");
        for (int i = 0; i < stats.getLength(); i++) {
            Node stat = stats.item(i);
            if (stat.getAttributes().item(0).getTextContent().equals("first_half_time"))
                first_half = stat.getFirstChild().getNodeValue();
            else if (stat.getAttributes().item(0).getTextContent().equals("second_half_time"))
                second_half = stat.getFirstChild().getNodeValue();
        }
    }

    private void set_LineUp(F7_Team team)
    {
        // Parse <TeamData>
        NodeList list = doc.getElementsByTagName("TeamData");
        int teamIndex = 0;
        for (int i = 0; i < list.getLength(); i++) {
            NamedNodeMap attrs = list.item(i).getAttributes();
            if (attrs.getNamedItem("Side").getNodeValue().equals(team.side)) {
                teamIndex = i;
                team.uID_ref = attrs.getNamedItem("TeamRef").getNodeValue();
                break;
            }
        }

        // Init Players from <PlayerLineUp>
        list = doc.getElementsByTagName("PlayerLineUp").item(teamIndex).getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            NamedNodeMap playerAttrs = list.item(i).getAttributes();
            if (playerAttrs == null)
                continue;

            String pId = playerAttrs.getNamedItem("PlayerRef").getNodeValue();
            String pPos = playerAttrs.getNamedItem("Position").getNodeValue();
            String pNumber = playerAttrs.getNamedItem("ShirtNumber").getNodeValue();
            String pStatus = playerAttrs.getNamedItem("Status").getNodeValue();
            boolean pCap = playerAttrs.getNamedItem("Captain") != null;
            team.players.put(pId, new F7_Player(pId, pPos, pNumber, pStatus, pCap));
        }

        // Get right <Team>
        list = doc.getElementsByTagName("Team");
        NodeList teamChilds = null;
        for (int i = 0; i < list.getLength(); i++) {
            NamedNodeMap attrs = list.item(i).getAttributes();
            if (attrs != null && attrs.getNamedItem("uID").getNodeValue().equals(team.uID_ref)) {
                teamChilds = list.item(i).getChildNodes();
                break;
            }
        }

        // Go through list and find items
        for (int i = 0; i < teamChilds.getLength(); i++) {
            Node curr = teamChilds.item(i);
            switch (curr.getNodeName()) {
                case "Name":
                    // Get TeamName
                    team.name = curr.getChildNodes().item(0).getNodeValue();
                    break;
                case "Player":
                    // Get playerName
                    F7_Player player = team.players.get(curr.getAttributes().getNamedItem("uID").getNodeValue());
                    String[] names = curr.getTextContent().trim().replaceAll("  ", "").split("\n");

                    player.firstName = names[0];
                    if (names.length == 2) {
                        player.lastName = names[1];
                    } else {
                        player.knownName = names[1];
                        player.lastName = names[2];
                    }
                    break;
                default:
            }
        }
    }

    public void addData(DataPool pool)
    {
        pool.games.add(new Table_Game(game_uID, homeTeam.uID_ref, guestTeam.uID_ref, first_half, second_half));
        homeTeam.addData(pool, game_uID);
        guestTeam.addData(pool, game_uID);
    }

    @Override
    public String toString()
    {
        return "game_uId: " + game_uID + '\n'
                + "first_half: " + first_half + '\n'
                + "second_half: " + second_half + '\n'
                + "home_team " + homeTeam.toString();
    }

    static class F7_Team {
        final String side;
        private final HashMap<String, F7_Player> players = new HashMap<>();
        String uID_ref, name;

        F7_Team(String side)
        {
            this.side = side;
        }

        void addData(DataPool pool, String game_uID)
        {
            pool.add(new Table_Team(uID_ref, name));
            for (F7_Player player : players.values()) {
                player.addData(pool, game_uID, uID_ref);
            }
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder("uID: " + uID_ref + "\nName:" + name + "\nSide:" + side + "\n");
            for (F7_Player player : players.values()) {
                builder.append(player.toString());
                builder.append("\n");
            }
            return builder.toString();
        }
    }


    static class F7_Player {

        final String refId, position, shirtNumber, status;
        final boolean captain;
        String firstName, knownName, lastName;

        F7_Player(String refId, String position, String shirtNumber, String status, boolean captain)
        {
            this.refId = refId;
            this.position = position;
            this.shirtNumber = shirtNumber;
            this.status = status;
            this.captain = captain;
        }

        void addData(DataPool pool, String game_uID, String team_ID)
        {
            pool.players.add(new Table_Player(refId, firstName, knownName, lastName));
            pool.gamePlayers.add(new Table_GamePlayer(game_uID, refId, team_ID, position, shirtNumber, status, captain));
        }

        @Override
        public String toString()
        {
            return "\tFirst: " + firstName + "\n\tKnown: " + knownName + "\n\tLast: " + lastName + "\n\tPosition: " + position;
        }
    }

}

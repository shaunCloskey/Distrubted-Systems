
import java.util.LinkedList;
import java.util.List;


public class RoutingInfoProt {
	
//pusedo code	
/*	when a table is received by process p1 from process p2:
		for each row in the received table:
		if address is not known by p1:
		add the address to p1's table with the link "p2" and a
		cost of one more than the received cost
		if 1 + cost for the address is better than the current known one:
		place this row in p1's table with the link "p2" and a
		cost of one more than the received cost
		if address is known by p1 with a link of p2 then:
		if the cost for p2 is not exactly one less than p1's cost:
		act as if this address was unknown to p1
		if process p1 has updated its table in any way:
		send updated table to all links */
	
	
}


class InputNode{
	String Name;
	int[] local_addresses;
	
	public InputNode (String n, int la[] ){
		this.Name = n;
		this.local_addresses = la;
	}
}

class InputLink {
	String left_name;
	String right_name;
	
	public InputLink (String nl, String nr){
		this.left_name = nl;
		this.right_name = nr;
	}
}

class InputCommand{
	String command_name;
	String process_name;
	
	public InputCommand(String c ,String p){
		this.command_name = c;
		this.process_name = p;
	}
}

class Input {
	List<InputNode> input_nodes = new LinkedList<InputNode>();
	List<InputLink> input_links = new LinkedList<InputLink>();
	List<InputCommand> input_commands = new LinkedList<InputCommand>();
	
	public Input(){
		int [] a = {1};
		int [] b = {2};
		int [] c = {3};
		int [] d = {4,5};
		input_nodes.add(new InputNode("p1", a));
		input_nodes.add(new InputNode("p1", b));
		input_nodes.add(new InputNode("p1", c));
		input_nodes.add(new InputNode("p1", d));
		input_links.add(new InputLink("p1", "p2"));
		input_links.add(new InputLink("p1", "p2"));
		input_links.add(new InputLink("p1", "p2"));
		input_links.add(new InputLink("p1", "p2"));
		input_commands.add(new InputCommand("send", "p1"));
	}
}


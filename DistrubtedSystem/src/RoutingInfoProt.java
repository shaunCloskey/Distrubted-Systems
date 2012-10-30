
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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
	
	
	/*
	 * need an object for each process that will handle the control of a specific process, spcafically sending of its tables and handling of the receive events
	 * 
	 */
	
	Map<String, Map< String, Values >> hash_maps = new HashMap<String, Map<String, Values>>();
	
	
	private void send(String process_name, Input input) {
		// TODO Auto-generated method stub
		
		//allLinks is the list of all links from sending process to any other link
		ArrayList<String> allLinks = new ArrayList<String>();
		
		for(InputLink inputLinks: input.input_links){
			if(inputLinks.left_name.equals(process_name)){
				//might not need this
				allLinks.add(inputLinks.right_name);
			}else{
				if(inputLinks.right_name.equals(process_name)){
					//might not need this
					allLinks.add(inputLinks.left_name);
					
				}
			}
		}
		
		for(String processToSend : allLinks){
			
		}
		
	}
	
	
	private void setupTable(Input input) {
		// TODO Auto-generated method stub
		for(InputNode inputNode: input.input_nodes){
			//create a hash table to enter to hash_maps
			//hash tables contains a hashmap for each process
			Map<String, Values> map = new HashMap<String,Values>();
			for(InputLink inputLinks: input.input_links){
				if(inputLinks.left_name.equals(inputNode.Name)){
					//might not need this
					Values value = new Values(int addressOfwhatIsLinkedTO, String whichProcessIsLinkedTo, int cost);
					map.put(which point it links to where to send, value);
				}else{
					if(inputLinks.right_name.equals(inputNode.Name)){
						//might not need this
						
						Values value = new Values(int addressOfwhatIsLinkedTO, String whichProcessIsLinkedTo, int cost);
						map.put(which point it links to where to send, value);
					}
				}
			}
		}
		for(InputLink inputLink: input.input_links){
			//add the link entry to the correct map
			//give it the correct info for its own links
		}
	}
	
	public void main () {
		Input input = new Input();
		
		setupTable(input);
		
		for(InputCommand inputCommand : input.input_commands ){
			send(inputCommand.process_name, input);
		}
	}
	
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
		input_nodes.add(new InputNode("p2", b));
		input_nodes.add(new InputNode("p3", c));
		input_nodes.add(new InputNode("p4", d));
		input_links.add(new InputLink("p1", "p2"));
		input_links.add(new InputLink("p1", "p4"));
		input_links.add(new InputLink("p2", "p3"));
		input_links.add(new InputLink("p3", "p4"));
		input_commands.add(new InputCommand("send", "p1"));
	}
}

class Values{
	int address;
	String link;
	int cost;
	
	public Values(int n_address, String n_link, int n_cost){
		this.address = n_address;
		this.cost = n_cost;
		this.link = n_link;
	}
}


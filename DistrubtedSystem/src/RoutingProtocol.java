
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RoutingProtocol {

	

	/*
	 * need an object for each process that will handle the control of a specific process, specifically sending of its tables and handling of the receive events
	 */
	
	static String send = "send";
	static String receive = "receive";
	
	//map of all tables key is the string of there process_name
	//the inner map is the table itself with the key being Integer.toString(node.address) for the destination node
	static Map<String, Map< String, Values >> hash_maps = new HashMap<String, Map<String, Values>>();
	
	
	//setup a queue to store all events. events are classes that store event type, and who is receiving what or sending what where
	static Stack<Events> queue = new Stack<Events>();
	
	
	//called after the nodes are all setup send command to start the whole process
	private static void send(String process_name) {
		
		//get the correct map
		Map<String, Values> routeTable = hash_maps.get(process_name);
		ArrayList <Events> sendEvents = new ArrayList<Events>(); 
		ArrayList <Events> removeEvents = new ArrayList<Events>();
		
		//for each entry in routeTable send the routeTable to the process name
		for(Map.Entry<String, Values> entry : routeTable.entrySet()){
			//set up event (send, p1, p2) for send p2 p1's table
			Values value = entry.getValue();
			String local = "local";
			if(!value.link.equals(local)){
				Events event = new Events(send, process_name, value.link);
				//update the queue with correct send event
				sendEvents.add(event);
			}
		}
		
		//this is an attempt to remove muliple sends to a process with more than 1 address as it is not need
		for(int i = 0; i<sendEvents.size()-1; i++){
			for(int j = 1; j<sendEvents.size()-i ;j++){
				if(sendEvents.get(i).right_process.equals(sendEvents.get(j).right_process)){
					removeEvents.add(sendEvents.get(j));
				}
			}
		}
		sendEvents.removeAll(removeEvents);
		for( Events event : sendEvents){
			queue.push(event);
		}
	}
	
	private static void receive(String p1, String p2){
		
		print(receive, p1,p2);
		
		boolean isUpdate = false;
		//for each row in the received table:
		Map<String, Values> receivedTable = hash_maps.get(p1);
		Map<String,Values> heldTable = hash_maps.get(p2);
		for(Map.Entry<String, Values> entry : receivedTable.entrySet()){
			
			//if address is not known by p2:
			if(!heldTable.containsKey(Integer.toString(entry.getValue().address))){
//				System.out.println( entry.getValue().address + " :address not known");
				//add the address to p2's table with the link "p1" and a
				//cost of one more than the received cost
				Values value = new Values(entry.getValue().address, p1, entry.getValue().cost +1);
				heldTable.put(Integer.toString(entry.getValue().address), value);
				isUpdate = true;
				
			}
			
			//if 1 + cost for the address is better than the current known one:
			if( (1+ entry.getValue().cost) < heldTable.get(entry.getKey()).cost ){
//				System.out.println("cost +1 better than currently known");
				//place this row in p2's table with the link "p1" and a
				//cost of one more than the received cost
				Values value = new Values(entry.getValue().address, p1, entry.getValue().cost +1);
				heldTable.put(Integer.toString(entry.getValue().address), value);
				isUpdate = true;
			}
			
			//if address is known by p2 with a link of p1 then:
			if(heldTable.containsKey(entry.getValue().address) && p1.equals(heldTable.get(entry.getKey()).link) ){
				//if the cost for p1 is not exactly one less than p2's cost:
//				System.out.println("address known with same link");
				int is_less = heldTable.get(entry.getKey()).cost - entry.getValue().cost;
				if( is_less != 1){
					System.out.println("cost is not exactly less than held");
					//act as if this address was unknown to p2
					//I.E.
					//add the address to p2's table with the link "p1" and a
					//cost of one more than the received cost
					Values value = new Values(entry.getValue().address, entry.getValue().link, entry.getValue().cost +1);
					heldTable.put(Integer.toString(entry.getValue().address), value);
					isUpdate = true;
				}
			}
		}
		if(isUpdate){
			send(p2);
		}
	}
	
	
	private static void setupTable(Input input) {

		
		
		//for each node create a hash map to store table
		for(InputNode inputNode: input.input_nodes){
			
			Map<String, Values> map = new HashMap<String,Values>();
			ArrayList<String> links = new ArrayList<String>();
			
			//assign the processes link to itself
			for(int i =0; i<inputNode.local_addresses.length;i++){
					Values value = new Values(inputNode.local_addresses[i], "local", 0);
					map.put(Integer.toString(inputNode.local_addresses[i]), value);
			}
			
			//store the names of all linked nodes
			for(InputLink inputLinks: input.input_links){
				if(inputLinks.left_name.equals(inputNode.Name)){
					//might not need this
					links.add(inputLinks.right_name);
				}else{
					if(inputLinks.right_name.equals(inputNode.Name)){
						//might not need this
						links.add(inputLinks.left_name);
					}
				}
			}
			
			//go through all links and add info to table
			for (String link : links){
				//address|nameLinkedTo| cost
				for(InputNode node:input.input_nodes ){
					//some processes may have more than one address
					if(link.equals(node.Name)){
						for(int i =0; i<node.local_addresses.length;i++){
							Values value = new Values(node.local_addresses[i], link, 1);
							map.put(Integer.toString(node.local_addresses[i]), value);
						}
					}
				}
			}
			
			
			
			//table for chose node is finished store map in hash maps.
			hash_maps.put(inputNode.Name, map);
		}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Input input = new Input();
		long time = System.currentTimeMillis();
		
		setupTable(input);
		for(InputCommand inputCommand : input.input_commands ){
			send(inputCommand.process_name);
		}
		
		print("table", "p1", "p1");
		print("table", "p2","p2");
		print("table", "p3", "p3");
		print("table", "p4","p4");
		
		
		
		
		//while the queue isn't empty keep popping off it when a new update arrives,
		//send events call receive on correct node, receive algorithm can add new send events to the back of stack
		//TODO
		boolean first = true;
		while(!queue.isEmpty()){
			
			Events event = queue.firstElement();
			queue.remove(0);
			if(event.event_type.equals(send)){
				//send the left and right process
				print(send, event.left_process, event.right_process);
				receive(event.left_process, event.right_process);
			}
			if(event.event_type.equals(receive)){
				//handle the receive event
				
			}
		}
		print("table", "p1", "p1");
		print("table", "p2","p2");
		print("table", "p3", "p3");
		print("table", "p4","p4");
		
		long finishTime = System.currentTimeMillis();
		System.out.println("time at start = " + time);
		System.out.println("time at end = " + finishTime);
		long timeTaken = finishTime - time;
		System.out.println("timein miliseconds to sort the table = " + timeTaken);
	}
	

	private static void print(String event_type, String left_process, String right_process) {
		if(event_type.equals(send)){
			System.out.print(send + " " + left_process + " " + right_process + " ");
			Map<String, Values> map = hash_maps.get(left_process); 
			for(Map.Entry<String, Values> entry : map.entrySet()){
				System.out.print("("+ entry.getValue().address+ "|" + entry.getValue().link + "|" + entry.getValue().cost + ")" + " ");
			}
			System.out.println("");
		}
		
		if(event_type.equals(receive)){
			System.out.print(receive + " " + left_process + " " + right_process + " ");
			Map<String, Values> map = hash_maps.get(left_process); 
			for(Map.Entry<String, Values> entry : map.entrySet()){
				System.out.print("("+ entry.getValue().address+ "|" + entry.getValue().link + "|" + entry.getValue().cost + ")" + " ");
			}
			System.out.println("");
		}
		
		if(event_type.equals("table")){
			System.out.print("table" + " " + left_process + " ");
			Map<String, Values> map = hash_maps.get(left_process); 
			for(Map.Entry<String, Values> entry : map.entrySet()){
				System.out.print("("+ entry.getValue().address+ "|" + entry.getValue().link + "|" + entry.getValue().cost + ")" + " ");
			}
			System.out.println("");
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

class Events{
	String event_type;
	String right_process;
	String left_process;
	
	public Events(String eventType, String leftProcess, String rightProcess){
		this.event_type = eventType;
		this.left_process = leftProcess;
		this.right_process = rightProcess;
	}
	
	
}

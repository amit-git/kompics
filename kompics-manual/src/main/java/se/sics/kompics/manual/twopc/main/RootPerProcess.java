package se.sics.kompics.manual.twopc.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Fault;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.Start;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.manual.twopc.client.ClientPort;
import se.sics.kompics.manual.twopc.client.Client;
import se.sics.kompics.manual.twopc.composite.CompositeTwoPC;
import se.sics.kompics.manual.twopc.event.ApplicationInit;
import se.sics.kompics.manual.twopc.event.CoordinatorInit;
import se.sics.kompics.manual.twopc.simple.TwoPCblob;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.mina.MinaNetwork;
import se.sics.kompics.network.mina.MinaNetworkInit;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

/**
 * The <code>ExecutionGroup</code> class.
 * 
 */
public class RootPerProcess extends ComponentDefinition {
	
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");
	}
	private static int selfId;
	private static String commandScript;
	Topology topology;

	Component time;
	Component network;
	Component twoPc;
	Component commandProcessor;

	
	private static final Logger logger = LoggerFactory
	.getLogger(RootPerProcess.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		if (args.length != 2)
		{
			System.out.println("Num args should have been 2. Was " + args.length);
			System.exit(-1);
		}
		
		selfId = Integer.parseInt(args[0]);
		commandScript = args[1];

		Kompics.createAndStart(RootPerProcess.class);
	}

	/**
	 * Instantiates a new assignment0 group0.
	 */
	public RootPerProcess() {
		
		String prop = System.getProperty("topology");
		topology = Topology.load(prop, selfId);
		
		// create components
		time = create(JavaTimer.class);
		network = create(MinaNetwork.class);
		if (selfId == 0)
		{
			twoPc = create(TwoPCblob.class);			
		}
		else
		{
			twoPc = create(CompositeTwoPC.class);
		}
		commandProcessor = create(Client.class);

		// handle possible faults in the components
		subscribe(handleFault, time.getControl());
		subscribe(handleFault, network.getControl());
		subscribe(handleFault, twoPc.getControl());
		subscribe(handleFault, commandProcessor.getControl());

		// XXX test that this topology is the same one generated by Application
		Address self = topology.getSelfAddress();
		Set<Address> neighborSet = topology.getNeighbors(self);

		// used to initialize the coordinator
		Map<Integer,Address> mapParticipantsAddrs = new HashMap<Integer,Address>();
		int pId = 0;
		for (Address addr : neighborSet) {
			pId++;
			mapParticipantsAddrs.put(pId,addr);
		}
		trigger(new MinaNetworkInit(self), network.getControl());
		
		connect(twoPc.getNegative(Network.class), network
				.getPositive(Network.class));
		connect(twoPc.getNegative(Timer.class), time
				.getPositive(Timer.class));
		
		connect(twoPc.getPositive(ClientPort.class), commandProcessor.getNegative(ClientPort.class));
		
		connect(commandProcessor.getNegative(Timer.class), time
				.getPositive(Timer.class));

		
		trigger(new CoordinatorInit(selfId, self, mapParticipantsAddrs), twoPc.getControl());
		trigger(new ApplicationInit(commandScript, neighborSet, self),
				commandProcessor.getControl());		
	}

	Handler<Fault> handleFault = new Handler<Fault>() {
		public void handle(Fault fault) {
			fault.getFault().printStackTrace(System.err);
		}
	};
	
}

/**
 * This file is part of the ID2210 course assignments kit.
 * 
 * Copyright (C) 2009 Royal Institute of Technology (KTH)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.sics.kompics.wan.master;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;



/**
 * The <code>BootstrapServerInit</code> class.
 * 
 * @author Cosmin Arad <cosmin@sics.se>
 */
public final class MasterInit extends Init {

	private final BootstrapConfiguration bootConfig;
//	private final P2pMonitorConfiguration monitorConfig;
	private final Address master;

        // , P2pMonitorConfiguration monitorConfig
	public MasterInit(Address master, BootstrapConfiguration bootconfig) {
		super();
		this.master = master;
		this.bootConfig = bootconfig;
//		this.monitorConfig = monitorConfig;
	}

	public Address getMaster() {
		return master;
	}
	
	public BootstrapConfiguration getBootConfig() {
		return bootConfig;
	}
	
//	public P2pMonitorConfiguration getMonitorConfig() {
//		return monitorConfig;
//	}
}

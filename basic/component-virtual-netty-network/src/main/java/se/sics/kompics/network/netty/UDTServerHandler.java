/* 
 * This file is part of the CaracalDB distributed storage system.
 *
 * Copyright (C) 2009 Swedish Institute of Computer Science (SICS) 
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
package se.sics.kompics.network.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.udt.UdtChannel;
import java.net.InetSocketAddress;
import se.sics.kompics.network.Transport;

/**
 *
 * @author Lars Kroll <lkroll@kth.se>
 */
@ChannelHandler.Sharable
public class UDTServerHandler extends StreamHandler {

    public UDTServerHandler(NettyNetwork component) {
        super(component, Transport.UDT);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        super.channelActive(ctx);
        UdtChannel channel = (UdtChannel) ctx.channel();
        component.addLocalSocket(channel.remoteAddress(), channel);
        InetSocketAddress other = channel.remoteAddress();
        channel.writeAndFlush(new DisambiguateConnection.Req(component.self, component.socketToAddress(other), protocol, other.getPort(), component.boundUDTPort));        
    }

}
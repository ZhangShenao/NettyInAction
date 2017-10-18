package ch11.cmd;

import io.netty.buffer.ByteBuf;

/**
 * 
 * <p>Description:CMD命令实体类,表示一个帧,包括命令名称和命令参数,之间用空格分隔</p>
 * @author ZhangShenao
 * @date 2017年10月18日
 */
public class Cmd {
	private ByteBuf name;	//命令名称
	private ByteBuf args;	//命令参数
	
	public Cmd(ByteBuf name, ByteBuf args) {
		this.name = name;
		this.args = args;
	}

	public ByteBuf getName() {
		return name;
	}

	public void setName(ByteBuf name) {
		this.name = name;
	}

	public ByteBuf getArgs() {
		return args;
	}

	public void setArgs(ByteBuf args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "Cmd [name=" + name + ", args=" + args + "]";
	}
	
	
}

package org.fugiczek.gwbot;

import com.sun.jna.Pointer;

public class GW2Tool {

	public static final GW2Tool INSTANCE = new GW2Tool();
	
	private GW2Tool(){}
	
	public float getSpeedValue(){
		Pointer handle = openProccess();
		int value = W32Api.ReadProcessMemory(handle, findSpeedValueAddress(), 4);
		W32Api.closeHandle(handle);
		return valueToFloat(value);
	}
	
	public void setSpeedValue(float value){
		Pointer handle = openProccess();
		System.out.println("Write success: " + W32Api.WriteProcessMemory(handle, findSpeedValueAddress(), new int[]{floatToValue(value)}, 4));
		W32Api.closeHandle(handle);
	}
	
	private int findSpeedValueAddress(){
		int bufferSize = 4;
		int baseAdd = 0x015d3c28;
		int offSet1=0x44;
		int offSet2=0x1C;
		int offSet3=0x170;
		Pointer handle = openProccess();
		int address =W32Api.ReadProcessMemory(handle, 
						W32Api.ReadProcessMemory(handle, 
								W32Api.ReadProcessMemory(handle, baseAdd, bufferSize)+offSet1, bufferSize)+offSet2, bufferSize)+offSet3;
		W32Api.closeHandle(handle);
		return address;
	}
	
	private Pointer openProccess(){
		int id = W32Api.GetWindowThreadProcessId(W32Api.FindWindow("Guild Wars 2"));
		return W32Api.OpenProcess(id);
	}
	
	private float valueToFloat(int value){
		Long ii = new Long(Long.parseLong(Integer.toHexString(value), 16));
		float f = Float.intBitsToFloat(ii.intValue());
		return f;
	}
	
	private int floatToValue(float value){
		int valu = Integer.parseInt(Integer.toHexString(Float.floatToRawIntBits(value)), 16);
		System.out.println("WriteTo -> " + valueToFloat(valu));
		return valu;
	}
	
}

package org.fugiczek.gwbot;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class W32Api {
	public static final int PROCESS_ALL_ACCESS = 0x1F0FFF;
	
    private interface User32 extends Library {   	
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
		
		Pointer FindWindowA(String winClass, String title); //najiti okna podle titulku
		int GetWindowThreadProcessId(Pointer hwnd, IntByReference refProcessId); //id vlakna procesu	
	}
	
    private interface Kernel32 extends Library {
    	
        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);
        boolean CloseHandle(Pointer handle);
        boolean ReadProcessMemory(Pointer hProcess, int inBaseAddress, Pointer outputBuffer, int nSize, IntByReference outNumberOfBytesRead);      
        boolean WriteProcessMemory(Pointer hProcess, int AdressToChange, int[] ValuesToWrite, int nSize, IntByReference ignore);
    }
    
    public static Pointer FindWindow(String title){
    	Pointer p = User32.INSTANCE.FindWindowA(null, title);
    	if(p!=null){
    		//System.out.println(title + " pointer -> " + p.toString());
        	return p;
    	}
    	return null;
    }
    
    public static int GetWindowThreadProcessId(Pointer hwnd){
    	IntByReference refProcessId = new IntByReference();
    	User32.INSTANCE.GetWindowThreadProcessId(hwnd, refProcessId);
    	//System.out.println("refProcessID -> " + refProcessId.getValue());   
    	return refProcessId.getValue();
    }
    
    public static Pointer OpenProcess(int dwProcessId){
    	Pointer p = Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS, false, dwProcessId);
    	//System.out.println("Opened ProcessID pointer -> " + p.toString());    	
    	return p;
    }
    
    public static boolean WriteProcessMemory(Pointer hProcess, int AdressToChange, int[] ValuesToWrite, int nSize){
    	IntByReference ignore = null;
    	return Kernel32.INSTANCE.WriteProcessMemory(hProcess, AdressToChange, ValuesToWrite, nSize, ignore);
    }
    
    public static int ReadProcessMemory(Pointer hProcess, int baseAddress, int bufferSize){
    	Memory outputBuffer = new Memory(bufferSize);
    	boolean success = Kernel32.INSTANCE.ReadProcessMemory(hProcess, baseAddress, outputBuffer, bufferSize, null);
    	//System.out.println("(0x"+ Integer.toHexString(baseAddress)+")Readed value -> " + outputBuffer.getInt(0));
    	if(success){
    		return outputBuffer.getInt(0);
    	}else{
    		return -1;
    	}
    }
    
    public static boolean closeHandle(Pointer handle){
    	return Kernel32.INSTANCE.CloseHandle(handle);
    }
    
}

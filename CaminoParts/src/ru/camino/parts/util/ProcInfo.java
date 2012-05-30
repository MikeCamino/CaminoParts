package ru.camino.parts.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Processor Information helper class.<br/>
 * Tries to retrieve current processor information like Vendor, Archtecture, Variant, Part, Revision, number of cores and BogoMIPS per core
	* @author Mike Camino
	*
 */
public class ProcInfo {
	private CPUInfo mCPUInfo;
	
	public ProcInfo() throws IOException {
		refresh();
	}
	
	/**
	 * Refreshes previously read CPU info
	 * @throws IOException when /proc/cpuinfo is inaccessible
	 */
	public void refresh() throws IOException {
		mCPUInfo = readCPUinfo();
	}
	
	public String getName() {
		return mCPUInfo.name;
	}
	
	public int getCoresCount() {
		return mCPUInfo.bogoMipsArr.size();
	}
	
	public float getBogoMIPS(int coreNumber) {
		return mCPUInfo.bogoMipsArr.get(coreNumber);
	}
	
	public float getBogoMIPSTotal() {
		float result = 0f;
		for (Float mips : mCPUInfo.bogoMipsArr) {
			result += mips;
		}
		return result;
	}
	
	public String getFeatures() {
		return mCPUInfo.features;
	}
	
	public String getCPUImplementer() {
		return mCPUInfo.implementer;
	}
	
	public String getCPUArchitecture() {
		return mCPUInfo.architecture;
	}
	
	public String getCPUVariant() {
		return mCPUInfo.variant;
	}
	
	public String getCPUPart() {
		return mCPUInfo.part;
	}
	
	public String getCPURevision() {
		return mCPUInfo.revision;
	}
	
	public String getHardware() {
		return mCPUInfo.hardware;
	}
	
	public String getRevision() {
		return mCPUInfo.hardwareRevision;
	}
	
	public String getSerial() {
		return mCPUInfo.serial;
	}
	
	/**
	 * Reads current CPU info
		* @return
	 * @throws IOException 
	 */
	private CPUInfo readCPUinfo() throws IOException {
		ProcessBuilder cmd;
		String result = "";

		String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
		cmd = new ProcessBuilder(args);

		Process process = cmd.start();
		InputStream in = process.getInputStream();
		byte[] re = new byte[1024];
		while (in.read(re) != -1) {
			result = result + new String(re);
		}
		in.close();
		
		CPUInfo cpuInfo = new CPUInfo();
		
		String[] resultArr = result.split("\n");
		
		for (int i = 0; i < resultArr.length; i++) {
			String line = resultArr[i];
			
			if (line.startsWith("Processor")) {
				cpuInfo.name = getValue(line);
			} else if (line.startsWith("BogoMIPS")) {
				cpuInfo.bogoMipsArr.add(Float.parseFloat(getValue(line)));
			} else if (line.startsWith("Features")) {
				cpuInfo.features = getValue(line);
			} else if (line.startsWith("CPU implementer")) {
				cpuInfo.implementer = getValue(line);
			} else if (line.startsWith("CPU architecture")) {
				cpuInfo.architecture = getValue(line);
			} else if (line.startsWith("CPU variant")) {
				cpuInfo.variant = getValue(line);
			} else if (line.startsWith("CPU part")) {
				cpuInfo.part = getValue(line);
			} else if (line.startsWith("CPU revision")) {
				cpuInfo.revision = getValue(line);
			} else if (line.startsWith("Hardware")) {
				cpuInfo.hardware = getValue(line);
			} else if (line.startsWith("Revision")) {
				cpuInfo.hardwareRevision = getValue(line);
			} else if (line.startsWith("Serial")) {
				cpuInfo.serial = getValue(line);
			}
		}
		
		return cpuInfo;
	}
	
	private String getValue(String line) {
		return line.split(":")[1].trim();
	}
	
	private class CPUInfo {
		String name;
		ArrayList<Float> bogoMipsArr = new ArrayList<Float>();
		String features;
		String implementer;
		String architecture;
		String variant;
		String part;
		String revision;
		String hardware;
		String hardwareRevision;
		String serial;
	}
}

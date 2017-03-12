package de.appwerft.netcdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiFileProxy;

import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.util.DebugFlags;

@Kroll.proxy(creatableInModule = NetcdfModule.class)
public class NetCDFProxy extends KrollProxy {
	// Standard Debugging variables
	private static final String LCAT = NetcdfModule.LCAT;
	private NetcdfFile $;

	// Constructor
	public NetCDFProxy() {
		super();
	}

	@Override
	public void handleCreationArgs(final KrollModule m, final Object[] p) {
		super.handleCreationArgs(m, p);
		String fileName = null;
		if (p.length == 1) {
			Object f = p[0];
			if (f instanceof TiFileProxy) {
				TiFileProxy tifileProxy = (TiFileProxy) f;
				// converting to filename
				if (!tifileProxy.exists()) {
					Log.e(LCAT, "file doesn't exists");
				} else {
					fileName = tifileProxy.getBaseFile().nativePath()
							.replace("file://", "");
				}
			} else if (f instanceof String) {
				fileName = ((String) f).replace("file://", "");
			} else
				Log.e(LCAT, "file not found" + fileName);
		} else {
			Log.e(LCAT, "file is null, you must provide file or filename");
			return;
		}
		if (fileName != null) {
			File f = new File(fileName);
			if (!f.exists()) {
				Log.e(LCAT, "file not found " + fileName);
				return;
			}
			openCDF(fileName);

		}
	}

	@Kroll.method
	public KrollDict getFileType() {
		KrollDict kd = new KrollDict();
		if ($ != null) {
			// http://www.unidata.ucar.edu/software/thredds/current/netcdf-java/reference/formats/FileTypes.html
			kd.put("id", $.getFileTypeId());
			kd.put("version", $.getFileTypeVersion());
			kd.put("description", $.getFileTypeDescription());
		} else
			kd.put("error", true);
		return kd;

	}

	@Kroll.method
	public String getDetailInfo() {
		return $.getDetailInfo();
	}

	@Kroll.method
	public String getCDL() {
		if ($ != null)
			return $.toString();
		else
			return null;
	}

	@Kroll.method
	public Object[] getVariables() {
		if ($ == null)
			return null;
		List<KrollDict> res = new ArrayList<KrollDict>();
		List<Variable> vars = $.getVariables();
		for (Variable var : vars) {
			KrollDict kd = new KrollDict();
			kd.put("type", var.getDataType());
			kd.put("description", var.getDescription());
			kd.put("elementSize", var.getElementSize());
			kd.put("DODSName", var.getDODSName());
		}
		return res.toArray();
	}

	@Kroll.method
	public Object[] getDimensions() {
		List<Dimension> dimensions = $.getDimensions();
		List<KrollDict> res = new ArrayList<KrollDict>();
		for (Dimension dim : dimensions) {
			KrollDict kd = new KrollDict();
			kd.put("fullName", dim.getFullName());
			kd.put("length", dim.getLength());
		}
		return res.toArray();
	}

	private void openCDF(String fn) {
		NetcdfFile.setDebugFlags(new DebugFlags() {
			public boolean isSet(String flag) {
				if (flag.equals("NetcdfFile/debugSPI")) {
					return true;
				}
				return false;
			}

			@Override
			public void set(String flag, boolean arg1) {
			}
		});
		// https://www.unidata.ucar.edu/software/thredds/current/netcdf-java/tutorial/NetcdfFile.html
		try {
			if (NetcdfFile.canOpen(fn)) {
				$ = NetcdfFile.open(fn);

			} else
				Log.w(LCAT, "cannot open NetCDF");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (null != $)
				try {
					$.close();
				} catch (IOException e) {
				}
		}

		// NetcdfFile - try iosp = ucar.nc2.iosp.bufr.BufrIosp2
		// NetcdfFile - NetcdfFile uses iosp =
		// BufrIosp2 java.io.IOException: BUFR file has incomplete tables
		// at ucar.nc2.NetcdfFile.open(NetcdfFile.java:427)
		// at ucar.nc2.NetcdfFile.open(NetcdfFile.java:394)
		// at ucar.nc2.NetcdfFile.open(NetcdfFile.java:381)
	}
}
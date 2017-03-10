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
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiFileProxy;

import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

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
	public void handleCreationArgs(KrollModule m, Object[] p) {
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
					fileName = tifileProxy.getBaseFile().nativePath();
				}
			} else if (f instanceof String) {
				fileName = ((String) f).replace("file://", "");
			} else
				Log.e(LCAT, "file not found");
		}
		if (fileName != null) {
			File f = new File(fileName);
			if (!f.exists()) {
				Log.e(LCAT, "file not found " + fileName);
			}
			try {
				$ = NetcdfFile.open(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				if (hasListeners("onerror")) {
					fireEvent("onerror", new KrollDict());
				}
			} finally {
				try {
					$.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Kroll.method
	public KrollDict getFileType() {
		// http://www.unidata.ucar.edu/software/thredds/current/netcdf-java/reference/formats/FileTypes.html
		KrollDict kd = new KrollDict();
		kd.put("id", $.getFileTypeId());
		kd.put("version", $.getFileTypeVersion());
		kd.put("description", $.getFileTypeDescription());
		return kd;
	}

	@Kroll.method
	public String getDetailInfo() {
		return $.getDetailInfo();
	}

	@Kroll.method
	public String getCDL() {
		return $.toString();
	}

	@Kroll.method
	public Object[] getVariables() {
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
}
#Ti.NetCDF <img src="https://clas-pages.uncc.edu/techne/wp-content/uploads/sites/93/2013/12/netcdf.png" width=200 />

This the Titanium version of NetCDF. 

NetCDF is a set of software libraries and self-describing, machine-independent data formats that support the creation, access, and sharing of array-oriented scientific data.

##Usage

```javascript
var CDFModule = require("de.appwerft.netcdf")
var CDF = CDFModule.createNetCDF(PATH_TO_CDF_FILE);
CDF.getFileType();
```
These are possible types:
```javascript
CDFModule.DATATYPE_INT
CDFModule.DATATYPE_DOUBLE
CDFModule.DATATYPE_FLOAT
CDFModule.DATATYPE_CHAR
CDFModule.DATATYPE_STRING
CDFModule.DATATYPE_UINT8
CDFModule.DATATYPE_SHORT
```

```javascript
CDF.getDetailInfo();
CDF.getVariables()
 // ...
 
CDF.close();
```
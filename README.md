#Ti.NetCDF

is the titanium version of NetCDF. 

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
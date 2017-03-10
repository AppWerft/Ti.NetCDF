#Ti.NetCDF

is the titanium version of NetCDF. 

##Usage

```javascript
var CDF = require("de.appwerft.netcdf").createNetCDF(PATH_TO_CDF_FILE);

CDF.getFileType();
CDF.getDetailInfo();
CDF.getVariables()
 // ...
 
CDF.close();
```
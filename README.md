# DaBaDEx

DaBaDEx is a tool to download text data from and/or upload text data into a database.

## Outset

Imagine for a moment, that you need to repeatedly query a database to extract data. Or, that you regularly need to upload data into a database. Or both. In comes DaBaDEx. It can either put (mode `-p`) or get (mode `-g`) data into or from a database, respectively. DaBaDEx uses plain old csv files as input (mode `-p`) or output (mode `-g`), respectively.

## Usage

Before use you have to compile the source code into a java archive. Assuming the compiled java archive has the name `DaBaDEx.jar` type

`java -jar DaBaDEx.jar -p {parameterfile}` for mode 'put' (see below) or

`java -jar DaBaDEx.jar -g {parameterfile}` for mode 'get'.

### Switch (Mode)

DaBaDEx uses one switch (`-g` for 'get' and `-p` for 'put') and one corresponding parameter file (please see 'test-g.prm' and 'test-p.prm' for sample files for modes 'get' and 'put', respectively) to handle data transfers. 

### Parameter file

The commented parameter files (see [test-g.prm](test-g.prm) and [test-p.prm](test-p.prm) for modes 'get' and 'put', respectively) are almost self-explanatory. The following hints should get you started quickly:

  * lines starting with a hash ('#') are comments
  * `dataFile` (mode 'put') - this can be a directory or a file; if a directory is given, only the latest file within the directory is processed. Latter behaviour is helpful if the names of the files to be processed change (e.g. are added a timetstamp etc.).
  * numeric fields in `fields` (mode 'put'): integer, double, real, etc. have to be suffixed with a single apostrophe to avoid them being surrounded by quotes during upload, like so: `fieldname'`
  * `action` (mode 'put') - depending on the primary key-gs in the receiving table of the database DaBaDEx can behave in two different ways when trying to upload entries already present: it can either replace fields other than the key(s) (`action = update`) or leave them untouched (`action = keep`).
  * `driver` - please obtain the correct JDBC driver (see the [JDBC driver Wikipedia entry](https://en.wikipedia.org/wiki/JDBC_driver)) for your database and enter the driver here; e.g. for PostgreSQL the entry will be `org.postgresql.Driver` and for MySQL `com.mysql.jdbc.Driver`.  
  
### Further things to consider

  * I wrote this little program many years ago (maybe 2008) and have not developed it since. For me, it has worked wonders and I still use it today. Nevertheless, you may find it dated.
  * DaBaDEx processes can be chained, i.e. you can follow up a process of mode 'get' with one of mode 'put'. This way you can use DaBaDEx to get data from one database and put it into another!
  * DaBaDEx currently does not take care of intermediary files and log files after processing. Please consider adding a routine to clear or rotate those files regularly.

# DaBaDEx

DaBaDEx tool to exchange data between databases using a simple text file to store output or intermediate data.

## Outset

Imagine for a moment, that you need to repeatedly query database to extract data. Or that you need to regularly upload data to a database.
Or both. In comes DaBaDEx. It can both get (mode '-g') or put (mode '-p') text data stored as csv files from or into a database, respectively.

## Usage

Before use you have to compile the source code into a java archive. Assuming, the compiled java archive has the name `DaBaDEx.jar` you then type

`java -jar DaBaDEx.jar -p {parameterfile}` for mode 'put' (see below) or

`java -jar DaBaDEx.jar -g {parameterfile}` for mode 'get'.

### Switches (Mode)

DaBaDEx uses one switch ('-g' for 'get' and '-p' for 'put') and one corresponding parameter file (please see 'test-g.prm' and 'test-p.prm' for sample files for modes 'get' and 'put', respectively) to correctly handle data transfers. 

### Parameter files

The parameter files are almost self-explanatory. Just a couple of hints that should get you started:

  * lines starting with a hash ('#') are comments
  * `dataFile` - this can be a directory or a file; if a directory is given, only the latest file within the directory is processed. Latter behaviour is helpful if the names of the files to be processed change (e.g. are added a timetstamp etc.).
  * numeric fields in `fields`: integer, double, real, etc. have to be suffixed with a single apostrophe to avoid them being surrounded by quotes during upload
  * `action` - depending on the primary keys in the receiving table of the database DaBaDEx can behave in two different ways when trying to upload entries already present: it can replace fields other than the key(s) (`action = update`) or it can leave them untouched (`action = keep`).
  * `driver` - please obtain the correct JDBC driver (see the [JDBC driver Wikipedia entry](https://en.wikipedia.org/wiki/JDBC_driver)) for your database and enter the driver here; e.g. for PostgreSQL the entry will be `org.postgresql.Driver` and for MySQL `com.mysql.jdbc.Driver` or similar.  
  
### Further things to consider

DaBaDEx currently does not take care of intermediary files and log files after processing. Please consider adding a routine to clear or rotate those files regularly.



  * Please ensure that intermediary files

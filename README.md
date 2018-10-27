# DaBaDEx

DaBaDEx tool to exchange data between databases using a simple text file to store output or intermediate data.

## Outset

Imagine for a moment, that you need to repeatedly query database to extract data. Or that you need to regularly upload data to a database.
Or both. In comes DaBaDEx. It can both get (mode '-g') or put (mode '-p') text data stored as csv files from or into a database, respectively.

## Modes

DaBaDEx uses a switch ('-g' for 'get' and '-p' for 'put') and corresponding parameter files (please see 'test-g.prm' and 'test-p.prm' for 
sample files for modes 'get' and 'put', respectively) to handle data transfers. The parameter files are almost self-explanatory.

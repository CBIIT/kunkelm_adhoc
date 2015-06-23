rm csvFiles/*.processed

for compound in 0501 0502 0503 0504 0505 0506 0507 0508 0509 0601 0602 0603 0604 0605 0606 0607 0608 0613 0614 0701 0702 0703 0704

do
	java parseStats $compound 
	java parseCensored $compound
	java parseEventTimes $compound
	java parseResponseSummary $compound	
done

cd /home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/csvFiles

rm statsForUpload.csv.processed
cat *stats.csv.processed >> statsForUpload.csv.processed

rm eventsForUpload.csv.processed
cat *events.csv.processed >> eventsForUpload.csv.processed

rm censoredForUpload.csv.processed
cat *censored.csv.processed >> censoredForUpload.csv.processed

rm summaryForUpload.csv.processed
cat *summary.csv.processed >> summaryForUpload.csv.processed


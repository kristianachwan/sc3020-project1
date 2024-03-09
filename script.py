import csv
from collections import Counter

# Path to your TSV file
tsv_file_path = './data.tsv'
# Output CSV file path
output_csv_file_path = 'numVotes_frequency.csv'

# Read the TSV file and count the frequency of numVotes
numVotes_frequency = Counter()
with open(tsv_file_path, mode='r', encoding='utf-8') as file:
    reader = csv.DictReader(file, delimiter='\t')
    for row in reader:
        numVotes = int(row['numVotes'])
        numVotes_frequency[numVotes] += 1

# Write the numVotes frequencies to a CSV file, sorted by numVotes
with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as file:
    writer = csv.writer(file)
    # Write the header
    writer.writerow(['numVotes', 'Frequency'])
    # Write the frequencies, sorted by numVotes
    for numVotes, frequency in sorted(numVotes_frequency.items()):
        writer.writerow([numVotes, frequency])

print(f'Done. Frequencies of numVotes have been written to {output_csv_file_path}')
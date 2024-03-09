import csv
from collections import defaultdict

# Path to your TSV file
tsv_file_path = './data.tsv'
# Output CSV file path
output_csv_file_path = 'numVotes_frequency.csv'

# Use defaultdict to keep track of the sum of ratings and count for each numVotes
ratings_data = defaultdict(lambda: {'count': 0, 'sum': 0.0})

with open(tsv_file_path, mode='r', encoding='utf-8') as file:
    reader = csv.DictReader(file, delimiter='\t')
    for row in reader:
        numVotes = int(row['numVotes'])
        averageRating = float(row['averageRating'])
        ratings_data[numVotes]['count'] += 1
        ratings_data[numVotes]['sum'] += averageRating

# Prepare data for writing, including calculating the average rating
output_data = []
for numVotes, data in ratings_data.items():
    average_rating = data['sum'] / data['count']
    output_data.append((numVotes, data['count'], average_rating))

# Sort the output data by numVotes before writing
output_data.sort()

# Write the data to a CSV file
with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as file:
    writer = csv.writer(file)
    # Write the header
    writer.writerow(['numVotes', 'Frequency', 'AverageRating'])
    # Write the data
    for numVotes, frequency, average_rating in output_data:
        writer.writerow([numVotes, frequency, average_rating])

print(f'Done. Frequencies and average ratings of numVotes have been written to {output_csv_file_path}, sorted by numVotes.')
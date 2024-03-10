import csv
from collections import defaultdict


tsv_file_path = './data.tsv'

output_csv_file_path = 'numVotes_frequency.csv'


ratings_data = defaultdict(lambda: {'count': 0, 'sum': 0.0})

with open(tsv_file_path, mode='r', encoding='utf-8') as file:
    reader = csv.DictReader(file, delimiter='\t')
    for row in reader:
        numVotes = int(row['numVotes'])
        averageRating = float(row['averageRating'])
        ratings_data[numVotes]['count'] += 1
        ratings_data[numVotes]['sum'] += averageRating


output_data = []
for numVotes, data in ratings_data.items():
    average_rating = data['sum']
    output_data.append((numVotes, data['count'], average_rating))


output_data.sort()

with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as file:
    writer = csv.writer(file)

    writer.writerow(['numVotes', 'Frequency', 'AverageRating'])

    for numVotes, frequency, average_rating in output_data:
        writer.writerow([numVotes, frequency, average_rating])

print(f'Done. Frequencies and average ratings of numVotes have been written to {output_csv_file_path}, sorted by numVotes.')
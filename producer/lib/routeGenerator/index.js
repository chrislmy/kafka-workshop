const fs = require('fs')
const csv = require('fast-csv');
const path = require('path');

const getCityData = (fileName) => new Promise((resolve, reject) => {
  const data = [];
  const filePath = path.join(__dirname, `../../../${fileName}.csv`);
  console.log(filePath);

  fs.createReadStream(filePath)
    .pipe(csv.parse({ headers: true }))
    .transform((data) => {
      const { city, lat, lng, country, iso2 } = data;
      return {
        city,
        lat,
        lng,
        country,
        countryCode: iso2,
      }
    })
    .on('error', error => {
      console.error(`Failed to read csv file line: ${error}`)
      reject('Failed to read cities csv file');
    })
    .on('data', row => data.push(row))
    .on('end', () => {
      console.log(`Finished reading ${data.length} cities`);
      resolve(data);
    });
});

const generateRoutes = (minBatchSize, maxBatchSize, maxRouteSize, cities) => {
  const routes = [];
  const batchSize = Math.floor(Math.random() * maxBatchSize) + 1;

  for (let i = 0; i < batchSize; i++) {
    const routeSize = Math.floor(Math.random() * (maxRouteSize - minBatchSize + 1)) + minBatchSize;
    const route = [];

    for (let j = 0; j < routeSize; j++) {
      const rowIndex = Math.floor(Math.random() * cities.length);
      route.push(cities[rowIndex]);
    }

    routes.push(route);
  }

  return routes;
}

module.exports = {
  getCityData,
  generateRoutes,
};
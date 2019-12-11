// Load the MySQL pool connection
const pool = require('../data/config');

// Display all users
app.get('/laptops', (request, response) => {
    pool.query('SELECT * FROM laptop', (error, result) => {
        if (error) throw error;

        response.send(result);
    });
});

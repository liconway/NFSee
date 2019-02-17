const express = require('express');
const app = express();
const PORT = 8192;

const MongoClient = require('mongodb').MongoClient;

const fs = require('fs');
const secure = JSON.parse(fs.readFileSync('./secure.json', 'UTF-8'));
const util = require('util');

const loginData = {
    host: secure.host,
    db: secure.db,
    user: secure.user,
    pass: secure.pass
}

app.get('/data', (req, res) => {
    const { uuid } = req.query;

    console.log("Started connection to MongoDB for UUID %s", uuid);
    MongoClient.connect(util.format(loginData.host, loginData.user, loginData.pass, loginData.db), { useNewUrlParser: true }, function(err, db) {
        if(err) throw err;
        else console.log("Connected to MongoDB");
    
        var dbo = db.db(loginData.db);
        var query = { uuid: uuid };
        dbo.collection('locations').find(query).toArray((err, result) => {
            if(err) {
                console.log("An error has occurred")
                res.status(500).end();
                throw err;
            }

            if(result.length === 0) {
                console.log("UUID not found")
                res.status(404).end();
                return;
            } else if(result.length > 1) {
                console.log("Two results with the same UUID found")
                res.status(409).end();
                return;
            }

            var info = result[0];
            delete info['_id'];
            info.alerts = {};

            dbo.collection('alerts').find({}).toArray((err, nresult) => {
                for(var i = 0; i < nresult.length; i++) {
                    var r = nresult[i];
                    if(info.latitude > (r.latitude - r.radius) && info.latitude < (r.latitude + r.radius)
                    && info.longitude > (r.longitude - r.radius) && info.longitude < (r.longitude + r.radius)) {
                        delete r['_id'];
                        info.alerts[r.uuid] = r;
                        delete info.alerts[r.uuid].uuid;

                        res.status(200).json(info);
                        db.close();
                        return;
                    }
                }

                res.status(200).json(info);
                db.close();
            })
        })
    })
})

app.set('json spaces', 4);
app.listen(PORT, () => {
    console.log(`NFSee API Server started on port ${PORT}`)
})
var SlackBot = require('slackbots');
var mongojs = require('mongojs');
var moment = require('moment');

var slackToken = process.env.SLACK_TOKEN;
var mongoUrl = process.env.MONGO_URL;
var botName = process.env.BOT_NAME || 'CoffeeBot';

if(!slackToken){
  console.log('Missing SLACK_TOKEN!');
  process.exit(1);
}

if(!mongoUrl){
  console.log('Missing MONGO_URL!');
  process.exit(2);
}

var db = mongojs(mongoUrl, ['machines']);

// Create bot
var bot = new SlackBot({
    token: slackToken,
    name: botName
});

var params = {
    icon_emoji: ':coffee:'
};

bot.on('start', function() {
    console.log('CoffeeBot ready for a cup of Joe!');
});

bot.on('stop', function(data) {
    console.log('Slack closed the connection');
    console.log('This is what I know: ' + data);

    bot.login();
});

bot.on('message', function(data){
  if(data.type === 'message' && data.username !== botName && /^!coffee/.test(data.text)){
    var args = data.text.split(" ");

    if(args[0] === '!coffee' && args.length === 1){
      provideStatus(data);
    } else {
      parseArgs(data.text, data);
    }
  }
});

// Actual useful functions
var provideStatus = function(data){
  console.log('Sending status update to channel', data.channel);

  db.machines.find(function (err, docs) {
    if(err){
      bot.postMessage(data.channel, "Could not get status. Try again later!", params);
    } else {
      var par = JSON.parse(JSON.stringify(params)); // Clone object
      par.attachments = formatMachines(docs);
      bot.postMessage(data.channel, 'Coffee machine status', par);
    }
  });
};

var parseArgs = function(argString, data){
  var args = argString.split(" ");
  if(args.length === 3){
    var status = args[2].toUpperCase();

    if(status === 'UP' || status === 'DOWN' || status === 'UNSTABLE'){
      console.log('Should update status for ' + args[1] + ' to ' + status);
      updateStatus(args[1], status, data);
    }
  } else {
    bot.postMessage(data.channel, 'I don\'t know to handle that one... Try !coffee', params);
  }
};

var formatMachines = function(machines){
  var formatting = [];

  machines.forEach(function(machine){
    var m = { fields: [], color: 'good' };

    m.fields.push({ title: 'Name', value: machine.name, short: true });
    m.fields.push({ title: 'Status', value: machine.status, short: true });
    m.fields.push({ title: 'Last updated', value: moment(machine.date).fromNow(), short: true });

    if(machine.status === 'DOWN') m.color = 'danger';
    if(machine.status === 'UNSTABLE') m.color = 'warning';

    formatting.push(m);
  });

  return formatting;
};

var updateStatus = function(machineName, status, data){
  var regex = new RegExp(["^", machineName, "$"].join(""), "i");
  var updated = new Date();

  db.machines.findAndModify({
    query: { name: { $regex: regex } },
    update: { $set: { status: status, date: updated } },
    new: true
  }, function (err, doc, lastErrorObject) {
    if(err || !lastErrorObject.updatedExisting){
      bot.postMessage(data.channel, 'Could not fetch information for machine ' + machineName, params);
    } else {
      bot.postMessage(data.channel, 'Marked ' + machineName + ' as ' + status, params);
    }
  });
};

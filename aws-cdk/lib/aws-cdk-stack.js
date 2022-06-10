const { Stack, RemovalPolicy, CfnOutput} = require('aws-cdk-lib');
const eb = require('aws-cdk-lib/aws-elasticbeanstalk');
const iam = require('aws-cdk-lib/aws-iam');
const acm = require('aws-cdk-lib/aws-certificatemanager');
const elbv2 = require('aws-cdk-lib/aws-elasticloadbalancingv2');
const ec2 = require('aws-cdk-lib/aws-ec2');
const db = require('aws-cdk-lib/aws-dynamodb')
const ag = require("aws-cdk-lib/aws-autoscaling");

class NexScoreStack extends Stack {
  constructor(scope, id, props) {
    super(scope, id, props);

    const application = new eb.CfnApplication(this, 'Application', {
      applicationName: 'NexScore'
    });

    // This is the role that your application will assume
    const ebRole = new iam.Role(this, 'CustomEBRole', {
      assumedBy: new iam.ServicePrincipal('ec2.amazonaws.com'),
    });

    // This is the Instance Profile which will allow the application to use the above role
    const ebInstanceProfile = new iam.CfnInstanceProfile(this, 'CustomInstanceProfile', {
      roles: [ebRole.roleName],
    });

    ebRole.addToPolicy(new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      resources: ['*'],
      actions: ['elasticbeanstalk:PutInstanceStatistics']
    }));

    const cert = new acm.Certificate(this, 'Certificate', {
      domainName: 'api.ploinky.de',
      validation: acm.CertificateValidation.fromDns(), // Records must be added manually
    });

    const environment = new eb.CfnEnvironment(this, 'Environment', {
      environmentName: 'NexScore-env',
      applicationName: 'NexScore',
      solutionStackName: '64bit Amazon Linux 2 v3.2.12 running Corretto 11',
      optionSettings: [
        {
          namespace: 'aws:autoscaling:launchconfiguration',
          optionName: 'IamInstanceProfile',
          value: ebInstanceProfile.attrArn
        },
        {
          namespace: 'aws:autoscaling:launchconfiguration',
          optionName: 'InstanceType',
          value: 't2.micro'
        },
        {
          namespace: 'aws:elasticbeanstalk:application:environment',
          optionName: 'SERVER_PORT',
          value: '5000'
        },
        {
          namespace: 'aws:elasticbeanstalk:application:environment',
          optionName: 'DYNAMODB_ENDPOINT',
          value: 'https://dynamodb.eu-central-1.amazonaws.com'
        },
        {
          namespace: 'aws:elasticbeanstalk:application:environment',
          optionName: 'ACCESS_KEY_ID',
          value: process.env.ACCESS_KEY_ID
        },
        {
          namespace: 'aws:elasticbeanstalk:application:environment',
          optionName: 'SECRET_ACCESS_KEY',
          value: process.env.SECRET_ACCESS_KEY
        },
        {
          namespace: 'aws:elasticbeanstalk:application:environment',
          optionName: 'RIOT_API_KEY',
          value: process.env.RIOT_API_KEY
        },
        {
          namespace: 'aws:elasticbeanstalk:application:environment',
          optionName: 'DYNAMODB_REGION',
          value: process.env.DYNAMODB_REGION
        },
        {
          namespace: 'aws:elasticbeanstalk:environment',
          optionName: 'LoadBalancerType',
          value: 'application'
        },
        {
          namespace: 'aws:elbv2:listener:443',
          optionName: 'ListenerEnabled',
          value: 'true'
        },
        {
          namespace: 'aws:elbv2:listener:default',
          optionName: 'ListenerEnabled',
          value: 'false'
        },
        {
          namespace: 'aws:elbv2:listener:443',
          optionName: 'SSLCertificateArns',
          value: cert.certificateArn
        },
        {
          namespace: 'aws:elbv2:listener:443',
          optionName: 'Protocol',
          value: 'HTTPS'
        }
      ]
    });

    environment.addDependsOn(application);

    const playerTable = new db.Table(this, 'Player', {
      removalPolicy: RemovalPolicy.DESTROY,
      partitionKey: {name: 'puuid', type: db.AttributeType.STRING},
      tableName: 'Player'
    });

    playerTable.grantFullAccess(ebRole);

    const matchTable = new db.Table(this, 'Match', {
      removalPolicy: RemovalPolicy.DESTROY,
      partitionKey: {name: 'matchid', type: db.AttributeType.STRING},
      tableName: 'Match'
    });

    matchTable.grantFullAccess(ebRole);

    const player2MatchTable = new db.Table(this, 'Player2Match', {
      removalPolicy: RemovalPolicy.DESTROY,
      partitionKey: {name: 'puuid', type: db.AttributeType.STRING},
      sortKey: {name: 'matchid', type: db.AttributeType.STRING},
      tableName: 'Player2Match'
    });

    player2MatchTable.grantFullAccess(ebRole);

    new CfnOutput(this, "elbDns", {
        id: "elbDns",
        value: environment.attrEndpointUrl,
        description: "DNS name of the created Elastic LoadBalancer",
        exportName: "elbDns"});
  };
}

module.exports = { NexScoreStack }

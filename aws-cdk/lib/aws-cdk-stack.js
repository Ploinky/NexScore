const { Stack, Duration, RemovalPolicy, aws_dynamodb} = require('aws-cdk-lib');
const aws_elasticbeanstalk = require('aws-cdk-lib/aws-elasticbeanstalk');
const iam = require('aws-cdk-lib/aws-iam');

class AwsCdkStack extends Stack {
  constructor(scope, id, props) {
    super(scope, id, props);

    const application = new aws_elasticbeanstalk.CfnApplication(this, 'Application', {
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

    const environment = new aws_elasticbeanstalk.CfnEnvironment(this, 'Environment', {
      environmentName: 'NexScore-env',
      applicationName: 'NexScore',
      solutionStackName: '64bit Amazon Linux 2 v3.2.12 running Corretto 11',
      optionSettings: [{
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
      }]
    });

    environment.addDependsOn(application);

    const playerTable = new aws_dynamodb.Table(this, 'Player', {
      removalPolicy: RemovalPolicy.DESTROY,
      partitionKey: {name: 'puuid', type: aws_dynamodb.AttributeType.STRING}
    });

    const matchTable = new aws_dynamodb.Table(this, 'Match', {
      removalPolicy: RemovalPolicy.DESTROY,
      partitionKey: {name: 'matchid', type: aws_dynamodb.AttributeType.STRING}
    });


  };
}

module.exports = { AwsCdkStack }

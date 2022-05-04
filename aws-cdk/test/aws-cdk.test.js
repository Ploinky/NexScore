const { AwsCdkStack } = require('../lib/aws-cdk-stack')
const cdk = require('aws-cdk-lib')
const { Capture, Match, Template} = require('aws-cdk-lib/assertions')
const iam = require("aws-cdk-lib/aws-iam");

test('Application created', () => {
    const app = new cdk.App();
    const stack = new AwsCdkStack(app, 'TestStack');

    const template = Template.fromStack(stack);

    template.hasResource("AWS::ElasticBeanstalk::Application",
    {
            "Type": "AWS::ElasticBeanstalk::Application",
            "Properties": {
                "ApplicationName": "NexScore"
            }
        }
    );

    template.hasResource("AWS::IAM::Role",
        {
            "Type": "AWS::IAM::Role",
            "Properties": {
                "AssumeRolePolicyDocument": {
                    "Statement": [
                        {
                            "Action": "sts:AssumeRole",
                            "Effect": "Allow",
                            "Principal": {
                                "Service": {
                                    "Fn::Join": [
                                        "",
                                        [
                                            "ec2.",
                                            {
                                                "Ref": "AWS::URLSuffix"
                                            }
                                        ]
                                    ]
                                }
                            }
                        }
                    ],
                    "Version": "2012-10-17"
                }
            }
        });

    template.hasResource("AWS::ElasticBeanstalk::Environment",
        {
            "Type": "AWS::ElasticBeanstalk::Environment",
            "Properties": {
                "EnvironmentName": "NexScore-env",
                "ApplicationName": "NexScore",
                "SolutionStackName": '64bit Amazon Linux 2 v3.2.12 running Corretto 11',
                "OptionSettings": [
                    {
                        "Namespace": 'aws:autoscaling:launchconfiguration',
                        "OptionName": 'IamInstanceProfile',
                        "Value": {
                            "Fn::GetAtt": [
                                "CustomInstanceProfile",
                                "Arn"
                            ]
                        }
                    },
                    {

                        "Namespace": 'aws:autoscaling:launchconfiguration',
                        "OptionName": 'InstanceType',
                        "Value": 't2.micro'
                    },
                    {
                        "Namespace": 'aws:elasticbeanstalk:application:environment',
                        "OptionName": 'SERVER_PORT',
                        "Value": '5000'
                    },
                    {
                        "Namespace": 'aws:elasticbeanstalk:application:environment',
                        "OptionName": 'DYNAMODB_ENDPOINT',
                        "Value": 'https://dynamodb.eu-central-1.amazonaws.com'
                    }]
            }
        }
    );

    template.hasResource("AWS::IAM::InstanceProfile",
        {
            "Type": "AWS::IAM::InstanceProfile",
            "Properties": {
                "Roles": [
                    {
                        "Ref": "CustomEBRoleCA0874C6"
                    }
                ]
            }

        }
    );

    template.hasResource("AWS::DynamoDB::Table", {
        "Type": "AWS::DynamoDB::Table",
        "Properties": {
            "KeySchema": [
                {
                    "AttributeName": "name",
                    "KeyType": "HASH"
                }
            ],
            "AttributeDefinitions": [
                {
                    "AttributeName": "name",
                    "AttributeType": "S"
                }
            ],
            "ProvisionedThroughput": {
                "ReadCapacityUnits": 5,
                "WriteCapacityUnits": 5
            },
            "TableName": "Player"
        },
        "UpdateReplacePolicy": "Delete",
        "DeletionPolicy": "Delete"
    });

    template.hasResource("AWS::DynamoDB::Table", {
        "Type": "AWS::DynamoDB::Table",
        "Properties": {
            "KeySchema": [
                {
                    "AttributeName": "matchid",
                    "KeyType": "HASH"
                }
            ],
            "ProvisionedThroughput": {
                "ReadCapacityUnits": 5,
                "WriteCapacityUnits": 5
            },
            "TableName": "Match"
        },
        "UpdateReplacePolicy": "Delete",
        "DeletionPolicy": "Delete"
    });

    template.hasResource("AWS::IAM::Policy",
        {
            "Properties": {
                "PolicyDocument": {
                    "Statement": [
                        {
                            "Action": "elasticbeanstalk:PutInstanceStatistics",
                            "Effect": "Allow",
                            "Resource": "*"
                        },
                        {
                            "Action": [
                                "dynamodb:BatchGetItem",
                                "dynamodb:GetRecords",
                                "dynamodb:GetShardIterator",
                                "dynamodb:Query",
                                "dynamodb:GetItem",
                                "dynamodb:Scan",
                                "dynamodb:ConditionCheckItem",
                                "dynamodb:BatchWriteItem",
                                "dynamodb:PutItem",
                                "dynamodb:UpdateItem",
                                "dynamodb:DeleteItem",
                                "dynamodb:DescribeTable"
                            ],
                            "Effect": "Allow",
                            "Resource": [
                                {
                                    "Fn::GetAtt": [
                                        "PlayerBCE11709",
                                        "Arn"
                                    ]
                                },
                                {
                                    "Ref": "AWS::NoValue"
                                }
                            ]
                        },
                        {
                            "Action": [
                                "dynamodb:BatchGetItem",
                                "dynamodb:GetRecords",
                                "dynamodb:GetShardIterator",
                                "dynamodb:Query",
                                "dynamodb:GetItem",
                                "dynamodb:Scan",
                                "dynamodb:ConditionCheckItem",
                                "dynamodb:BatchWriteItem",
                                "dynamodb:PutItem",
                                "dynamodb:UpdateItem",
                                "dynamodb:DeleteItem",
                                "dynamodb:DescribeTable"
                            ],
                            "Effect": "Allow",
                            "Resource": [
                                {
                                    "Fn::GetAtt": [
                                        "MatchE2EA7BB6",
                                        "Arn"
                                    ]
                                },
                                {
                                    "Ref": "AWS::NoValue"
                                }
                            ]
                        }
                    ],
                   "Version": "2012-10-17"
                },
            }
        }
    );
});
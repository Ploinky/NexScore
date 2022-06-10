const { NexScoreStack } = require('../lib/aws-cdk-stack')
const cdk = require('aws-cdk-lib')
const { Capture, Match, Template} = require('aws-cdk-lib/assertions')
const iam = require("aws-cdk-lib/aws-iam");
const {aws_dynamodb} = require("aws-cdk-lib");

test('Application created', () => {
    const app = new cdk.App();
    const stack = new NexScoreStack(app, 'TestStack');

    const template = Template.fromStack(stack);

    template.hasResource("AWS::ElasticBeanstalk::Application",
    {
            Properties: {
                ApplicationName: "NexScore"
            }
        }
    );

    template.hasResource("AWS::IAM::Role",
        {
            Properties: {
                AssumeRolePolicyDocument: {
                    Statement: [
                        {
                            Action: "sts:AssumeRole",
                            Effect: "Allow",
                            Principal: {
                                Service: {
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
                }
            }
        });

    template.hasResource("AWS::ElasticBeanstalk::Environment",
        {
            Properties: {
                EnvironmentName: "NexScore-env",
                ApplicationName: "NexScore",
                SolutionStackName: '64bit Amazon Linux 2 v3.2.12 running Corretto 11',
                OptionSettings: [
                    {
                        Namespace: 'aws:autoscaling:launchconfiguration',
                        OptionName: 'IamInstanceProfile',
                        Value: {
                            "Fn::GetAtt": [
                                "CustomInstanceProfile",
                                "Arn"
                            ]
                        }
                    },
                    {

                        Namespace: 'aws:autoscaling:launchconfiguration',
                        OptionName: 'InstanceType',
                        Value: 't2.micro'
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:application:environment',
                        OptionName: 'SERVER_PORT',
                        Value: '5000'
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:application:environment',
                        OptionName: 'DYNAMODB_ENDPOINT',
                        Value: 'https://dynamodb.eu-central-1.amazonaws.com'
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:application:environment',
                        OptionName: 'ACCESS_KEY_ID',
                        Value: process.env.ACCESS_KEY_ID
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:application:environment',
                        OptionName: 'SECRET_ACCESS_KEY',
                        Value: process.env.SECRET_ACCESS_KEY
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:application:environment',
                        OptionName: 'RIOT_API_KEY',
                        Value: process.env.RIOT_API_KEY
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:application:environment',
                        OptionName: 'DYNAMODB_REGION',
                        Value: process.env.DYNAMODB_REGION
                    },
                    {
                        Namespace: 'aws:elasticbeanstalk:environment',
                        OptionName: 'LoadBalancerType',
                        Value: 'application'
                    },
                    {
                        Namespace: 'aws:elbv2:listener:443',
                        OptionName: 'ListenerEnabled',
                        Value: 'true'
                    },
                    {
                        Namespace: 'aws:elbv2:listener:default',
                        OptionName: 'ListenerEnabled',
                        Value: 'false'
                    },
                    {
                        Namespace: 'aws:elbv2:listener:443',
                        OptionName: 'SSLCertificateArns',
                        Value: {
                            Ref: "Certificate4E7ABB08"
                        }
                    },
                    {
                        Namespace: 'aws:elbv2:listener:443',
                        OptionName: 'Protocol',
                        Value: 'HTTPS'
                    }
                ]
            }
        }
    );

    template.hasResource("AWS::IAM::InstanceProfile",
        {
            Properties: {
                Roles: [
                    {
                        "Ref": "CustomEBRoleCA0874C6"
                    }
                ]
            }
        }
    );

    template.hasResource("AWS::DynamoDB::Table", {
        Properties: {
            KeySchema: [
                {
                    AttributeName: "puuid",
                    KeyType: "HASH"
                }
            ],
            AttributeDefinitions: [
                {
                    AttributeName: "puuid",
                    AttributeType: "S"
                }
            ],
            TableName: "Player"
        },
        UpdateReplacePolicy: "Delete",
        DeletionPolicy: "Delete"
    });

    template.hasResource("AWS::DynamoDB::Table", {
        Properties: {
            KeySchema: [
                {
                    AttributeName: "matchid",
                    KeyType: "HASH"
                }
            ],
            AttributeDefinitions: [
                {
                    AttributeName: "matchid",
                    AttributeType: aws_dynamodb.AttributeType.STRING
                }
            ],
            ProvisionedThroughput: {
                ReadCapacityUnits: 5,
                WriteCapacityUnits: 5
            },
            TableName: "Match"
        },
        UpdateReplacePolicy: "Delete",
        DeletionPolicy: "Delete"
    });

    template.hasResource("AWS::DynamoDB::Table", {
        Properties: {
            KeySchema: [
                {
                    AttributeName: "puuid",
                    KeyType: "HASH"
                },
                {
                    AttributeName: "matchid",
                    KeyType: "RANGE"
                }
            ],
            AttributeDefinitions: [
                {
                    AttributeName: "puuid",
                    AttributeType: aws_dynamodb.AttributeType.STRING
                },
                {
                    AttributeName: "matchid",
                    AttributeType: aws_dynamodb.AttributeType.STRING
                },
            ],
            ProvisionedThroughput: {
                ReadCapacityUnits: 5,
                WriteCapacityUnits: 5
            },
            TableName: "Player2Match"
        },
        UpdateReplacePolicy: "Delete",
        DeletionPolicy: "Delete"
    });

    template.hasResource("AWS::IAM::Policy",
        {
            Properties: {
                PolicyDocument: {
                    Statement: [
                        {
                            Action: "elasticbeanstalk:PutInstanceStatistics",
                            Effect: "Allow",
                            Resource: "*"
                        },
                        {
                            Action: "dynamodb:*",
                            Effect: "Allow",
                            Resource: [
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
                            Action: "dynamodb:*",
                            Effect: "Allow",
                            Resource: [
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
                        },
                        {
                            Action: "dynamodb:*",
                            Effect: "Allow",
                            Resource: [
                                {
                                    "Fn::GetAtt": [
                                        "Player2MatchD1D576AF",
                                        "Arn"
                                    ]
                                },
                                {
                                    "Ref": "AWS::NoValue"
                                }
                            ]
                        }
                    ],
                },
            }
        }
    );

    template.hasResource("AWS::CertificateManager::Certificate", {
        Properties: {
            DomainName: "api.ploinky.de",
            ValidationMethod: "DNS"
        }
    });
});
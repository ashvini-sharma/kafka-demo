pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID = '562437414962'
        AWS_REGION = 'ap-south-1'
        S3_BUCKET = 'jenkins-project-springboot-artifacts'
        ECR_REPO = 'learnjenkinsrepo'
    }

    stages {

        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage('Build Jar') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-21'
                    reuseNode true
                }
            }
            steps {
                sh 'cd order-app && mvn clean package -DskipTests'
                sh 'cd payment-app && mvn clean package -DskipTests'
            }
        }

        stage('Build Project Image') {
            steps {
                sh '''
                docker build --platform linux/amd64 -t order-app:${BUILD_ID} order-app
                docker build --platform linux/amd64 -t payment-app:${BUILD_ID} payment-app
                '''
            }
        }

        stage('Debug Env before ECR Push') {
            steps {
                sh '''
                    echo AWS_REGION=$AWS_REGION
                    echo AWS_ACCOUNT_ID=$AWS_ACCOUNT_ID
                '''
            }
        }

        /*
        stage('Upload Image to ECR') {
            steps {
                script {
                    env.IMAGE_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${BUILD_ID}"
                    echo "IMAGE_URI=${env.IMAGE_URI}"
                }
        
                withCredentials([
                    usernamePassword(
                        credentialsId: 'Jenkins-user',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    )
                ]) {
                    sh '''
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                        docker tag $APP_NAME:$BUILD_ID $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:$BUILD_ID

                        docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:$BUILD_ID
                    '''
                }
            }
        }*/        

        stage('Upload Image to ECR') {
            steps {
                 script {
                    env.ORDER_IMAGE_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:order-app-${BUILD_ID}"
                    env.PAYMENT_IMAGE_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:payment-app-${BUILD_ID}"
                    echo "ORDER_IMAGE_URI=${env.ORDER_IMAGE_URI}"
                    echo "PAYMENT_IMAGE_URI=${env.PAYMENT_IMAGE_URI}"
                }

                withCredentials([
                    usernamePassword(
                        credentialsId: 'Jenkins-user',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    )
                ]) {
                    sh '''
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                        docker tag order-app:${BUILD_ID} ${ORDER_IMAGE_URI}
                        docker tag payment-app:${BUILD_ID} ${PAYMENT_IMAGE_URI}

                        docker push ${ORDER_IMAGE_URI}
                        docker push ${PAYMENT_IMAGE_URI}

                        sed "s|IMAGE_URI_PLACEHOLDER|${ORDER_IMAGE_URI}|g" deployment-order-app.yaml > deployment-order-app-prod.yaml
                        sed "s|IMAGE_URI_PLACEHOLDER|${PAYMENT_IMAGE_URI}|g" deployment-payment-app.yaml > deployment-payment-app-prod.yaml
                    '''
                }
            }
        }

        /*
        stage('Deploy To EKS') {
            steps {
                sh '''
                aws eks update-kubeconfig --name secure-cluster --region ${AWS_REGION}

                kubectl apply -f kafka-configmap.yaml
                kubectl apply -f kafka-service.yaml
                kubectl apply -f kafka-statefulset.yaml

                kubectl rollout status statefulset/kafka --timeout=600s

                kubectl apply -f deployment-kafkaUI.yaml
                kubectl apply -f service-kafkaUI.yaml

                kubectl apply -f deployment-order-app-prod.yaml
                kubectl apply -f service-order-app.yaml

                kubectl apply -f deployment-payment-app-prod.yaml
                kubectl apply -f service-payment-app.yaml
                '''
            }
        }

        stage('Create Topics') {
            steps {
                sh '''
                kubectl exec kafka-0 -- bash -c \
                "kafka-topics.sh --bootstrap-server kafka-0.kafka:9092 --list | grep -w order-topic" || \
                kubectl exec kafka-0 -- kafka-topics.sh --create \
                --topic order-topic \
                --bootstrap-server kafka-0.kafka:9092 \
                --partitions 3 \
                --replication-factor 2

                kubectl exec kafka-0 -- bash -c \
                "kafka-topics.sh --bootstrap-server kafka-0.kafka:9092 --list | grep -w payment-topic" || \
                kubectl exec kafka-0 -- kafka-topics.sh --create \
                --topic payment-topic \
                --bootstrap-server kafka-0.kafka:9092 \
                --partitions 3 \
                --replication-factor 2
                '''
            }
        }

        stage('Upload Jar To S3') {
            steps {
                sh 'aws s3 cp order-app/target/ s3://${S3_BUCKET}/order-app/ --recursive'
                sh 'aws s3 cp payment-app/target/ s3://${S3_BUCKET}/payment-app/ --recursive'
            }
        }
        */
    }

    post {
        success {
            echo 'Build completed and JAR uploaded.'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}

package com.example.collegeDate.service.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.util.UUID

@Service
class S3Service(
    @Value("\${aws.accessKeyId}") private val accessKeyId: String,
    @Value("\${aws.secretKey}") private val secretKey: String,
    @Value("\${aws.s3.bucketName}") private val bucketName: String,
    @Value("\${aws.s3.region}") private val region: String
) {
    private val s3Client: AmazonS3

    init {
        val awsCreds = BasicAWSCredentials(accessKeyId, secretKey)
        s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.fromName(region))
            .withCredentials(AWSStaticCredentialsProvider(awsCreds))
            .build()
    }

    fun uploadFileToS3(inputStream: InputStream, keyName: String): String {
      val id = UUID.randomUUID().toString()
        s3Client.putObject(bucketName, id, inputStream, null)
        return s3Client.getUrl(bucketName, id).toString()
    }
}
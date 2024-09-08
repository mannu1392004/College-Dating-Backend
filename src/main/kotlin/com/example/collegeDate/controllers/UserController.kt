package com.example.collegeDate.controllers

import com.example.collegeDate.model.outputmatchings.MatchingOutputs
import com.example.collegeDate.model.profiledetailstosend.ProfileDetailsToSend
import com.example.collegeDate.model.removeMatching.RemoveMatching
import com.example.collegeDate.model.sorting.SortingInput
import com.example.collegeDate.security.jwtutil.JwtUtil
import com.example.collegeDate.service.algo.MatchingAlgo
import com.example.collegeDate.service.database.DatabaseOperations
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val jwtUtil: JwtUtil,
    private val databaseOperations: DatabaseOperations,
    private val matchingAlgo: MatchingAlgo
) {



    // get matching present in the database
    @PostMapping("/getMatchedUsers")
    fun getMatchedUsers(
        authentication: Authentication,
        @RequestParam(defaultValue = "0") page: Int,  // Page number, default to 0
        @RequestParam(defaultValue = "10") size: Int   // Number of items per page, default to 10
    ): List<MatchingOutputs> {
        val username = authentication.name
        val user =
            databaseOperations.getUser(username) ?: return listOf(MatchingOutputs("user NotFound", "", emptyList()))

        val matchedUsers = user.matched ?: return listOf(MatchingOutputs("NotFound", "", emptyList()))

        val startIndex = page * size
        val endIndex = (page + 1) * size
        val paginatedMatchedUsers = matchedUsers.subList(startIndex, minOf(endIndex, matchedUsers.size))


        val list: MutableList<MatchingOutputs> = mutableListOf()

        paginatedMatchedUsers.forEach { x ->

            val user1 = databaseOperations.getUser(x)

            val commonInterests = user1?.interests?.intersect(user.interests)?.toList()



            user1?.cardPictureUrl?.let {
                MatchingOutputs(
                    username = x,
                    cardPicture = it,
                    common = commonInterests ?: emptyList()
                )
            }?.let {
                list.add(
                    it
                )
            }


        }

        return list
    }


    // sorting algo
    @PostMapping("/sort")
    fun sortProfiles(
        authentication: Authentication,
        @RequestBody sortingInput: SortingInput
    ): List<MatchingOutputs> {
        val username = authentication.name
        val user = databaseOperations.getUser(username) ?: return emptyList()

        if (sortingInput.gender == 2 && sortingInput.collegeId!=0) {
            val list = mutableListOf<MatchingOutputs>()
            databaseOperations.sortByCollegeName(collegeName = sortingInput.collegeId).forEach {
                if (user.requestsSent?.contains(it.username) == false) {
                    it.cardPictureUrl?.let { cardPictureUrl ->
                        list.add(
                            MatchingOutputs(
                                username = it.username,
                                cardPicture = cardPictureUrl,
                                common = user.interests.intersect(it.interests).toList()
                            )
                        )
                    }
                }
            }
            return list
        } else if (sortingInput.gender != 2 && sortingInput.collegeId!=0) {
            val list = mutableListOf<MatchingOutputs>()
            databaseOperations.sortByGender(gender = sortingInput.gender).forEach {
                if (user.requestsSent?.contains(it.username) == false)
                    it.cardPictureUrl?.let { cardPictureUrl ->
                        list.add(
                            MatchingOutputs(
                                username = it.username,
                                cardPicture = cardPictureUrl,
                                common = user.interests.intersect(it.interests).toList()
                            )
                        )
                    }
            }
            return list
        } else if (sortingInput.gender != 2 && sortingInput.collegeId!=0) {
            val list = mutableListOf<MatchingOutputs>()
            databaseOperations.sortByBoth(gender = sortingInput.gender, collegeName = sortingInput.collegeId).forEach {
                if (user.requestsSent?.contains(it.username) == false) {
                    it.cardPictureUrl?.let { cardPictureUrl ->
                        list.add(
                            MatchingOutputs(
                                username = it.username,
                                cardPicture = cardPictureUrl,
                                common = user.interests.intersect(it.interests).toList()
                            )
                        )
                    }
                }
            }
            return list
        } else {
            return emptyList()
        }
    }

    // left swipe remove from the list
    @PostMapping("/removeMatchedUser")
    fun removeMatchedUser(authentication: Authentication, @RequestBody removeMatching: RemoveMatching): String {
        val currentUser = authentication.name
        val user = databaseOperations.getUser(currentUser) ?: return "User not found"
        if (removeMatching.status == 0) {
            user.matched = user.matched?.filter { it != removeMatching.userName }
            databaseOperations.saveUser(user)
        }
        // sent request
        if (removeMatching.status == 1) {
            val likedUser = databaseOperations.getUser(removeMatching.userName) ?: return "Liked User Not Found"
            if (user.requests?.contains(likedUser.username) == true) {
                user.requests!!.remove(user.username)
                likedUser.requestsSent?.remove(user.username)
                user.profileAccess.add(likedUser.username)
                likedUser.profileAccess.add(user.username)
            } else {
                user.matched = user.matched?.filter { it != removeMatching.userName }
                likedUser.requests?.add(user.username)
                user.requestsSent?.add(likedUser.username)

            }
            databaseOperations.saveUser(user)
            databaseOperations.saveUser(likedUser)
        }
        return "User removed successfully"
    }

    @GetMapping("/getRequests")
    fun getRequests(authentication: Authentication): List<MatchingOutputs> {
        val username = authentication.name
        val currentUser = databaseOperations.getUser(username) ?: return emptyList()

        // Fetch requests from the current user
        val requests = currentUser.requests ?: return emptyList()

        // Retrieve users for the requests
        return requests.mapNotNull { requestUsername ->
            val requestedUser = databaseOperations.getUser(requestUsername)
            requestedUser?.cardPictureUrl?.let { cardPictureUrl ->
                MatchingOutputs(
                    username = requestUsername,
                    cardPicture = cardPictureUrl,
                    common = emptyList()
                )
            }
        }
    }


    //accept request
    @GetMapping("/acceptRequest")
    fun acceptRequest(
        authentication: Authentication,
        @RequestParam userName: String
    ): String {
        val username = authentication.name
        val currentUser = databaseOperations.getUser(username) ?: return "User not found"
        val requestedUser = databaseOperations.getUser(userName) ?: return "Requested User not found"

        if (requestedUser.requestsSent?.contains(currentUser.username) == true) {
            requestedUser.requestsSent?.remove(currentUser.username)
            currentUser.requests?.remove(requestedUser.username)
            currentUser.profileAccess.add(requestedUser.username)
            requestedUser.profileAccess.add(requestedUser.username)

            databaseOperations.saveUser(currentUser)
            databaseOperations.saveUser(requestedUser)
            return "Request Accepted"
        }


        return "Something failing our if condition to sent you request accepted"
    }


    // getTheProfile
    @PostMapping("/getTheProfile")
    fun getProfileDetails(
        authentication: Authentication,
        @RequestBody username: String
    ): ProfileDetailsToSend {

        val currentUser = authentication.name
        val user = databaseOperations.getUser(currentUser) ?: return ProfileDetailsToSend(error = "User not found")
        if (currentUser == username) {
            return ProfileDetailsToSend(
                username = username,
                cardPictureUrl = user.cardPictureUrl ?: "",
                error = "",
                bio = user.bio ?: "",
                collegeName = user.collegeName,
                interests = user.interests.toList(),
                postImageUrl = user.posts ?: mutableListOf()
            )
        } else {

            val requestedUser =
                databaseOperations.getUser(username) ?: return ProfileDetailsToSend(error = "Requested User not found")

            val commonInterests = requestedUser.interests.intersect(user.interests).toList()
            return ProfileDetailsToSend(
                username = requestedUser.username,
                cardPictureUrl = requestedUser.cardPictureUrl ?: "",
                commonInterests = commonInterests,
                profileAccess = user.profileAccess.contains(requestedUser.username),
                error = "",
                bio = requestedUser.bio ?: "",
                collegeName = requestedUser.collegeName,
                interests = requestedUser.interests.toList(),
                postImageUrl = requestedUser.posts ?: mutableListOf(),
                requestSent = user.requestsSent?.contains(requestedUser.username) ?: false
            )
        }
    }



}
# Dr. GS

I'm currently learning reactive programming with WebFlux and this is my first study of this topic.

The advantage of using this paradigm is to use lesser threads, achieving non-blocking asynchronous IO, streaming with backpressure which helps to build more scalable applications over all.

Please inspect the uses cases of Mono.zip(), Flux.merge() etc. in the code which gives rise to more efficient code (Parallelism).

Below is the database schema of this project:

![db_schema](db_schema.png)

## Work Flows

### 1.User Progress

#### 1. GetUserRequest: Parameters: [Integer userProgressId]

- If the user does not exist, return "customerNotFound" exception.
- Return the entity based on the parameter identified as "userProgressId".

#### 2. CreateUserRequest: Parameters: [TestGroup testGroup]

- Create the UserProgress entity based on the provided "testGroup" and default values.
- Return the saved entity.

#### 3. UpdateLevelRequest: Parameters: [Integer userProgressId, Instant timestamp]

- If the user does not exist, return "customerNotFound" exception.
- First find the userProgress entity by "userProgressId" and apply level-up() procedure.
- Then, if the timestamp is between 08:00 and 22:00 (UTC) and the user is at least 50 level,
then apply rewardHeliumConditionally() procedure.
- Finally, save the updated entity into database in a non-blocking way as always and return the saved entity to client.

### 2.Pop The Balloon Event

For all the requests below first check the followings: 

- if the "timestamp" is not between 08:00 and 22:00 (UTC), then return IllegalDate() exception.
- if the "eventId" is not 1 (default and unique entity for this demo), then return IllegalEvent() exception.
- After finding the UserProgress entity based on the "userProgressId", if the level is below 50, then return levelInsufficient() exception.

#### 1. GetSuggestionsRequest: Parameters: [Integer userProgressId, Integer eventId, Instant timestamp]

- If the requester is already matched, then return RequesterAlreadyCollaborated() exception.
- Definition of "available" users: Users that are at least 50 level with the same testGroup with the requester and, that have not matched already for this event and, that have not rejected the requester for this event and, that have not been sent invitation for this event by the requester yet.
- Basically return this "available" users as a randomized list to client.

#### 2. GetInvitationsRequest: Parameters: [Integer userProgressId, Integer eventId, Instant timestamp]

- If the requester is already matched, then return RequesterAlreadyCollaborated() exception.
- Return list of 'PENDING' Invitations received where the receiver is "userProgressId".
- I was going to return pageable object but I did not have enough time to do. But it is an easy upgrade and more performant.

#### 3. InvitePartnerRequest: Parameters: [Integer senderUserProgressId, Integer receiverUserProgressId, Integer eventId, Instant timestamp]

- If the requester(sender) is already matched, then return RequesterAlreadyCollaborated() exception.
- If the requested(receiver) is already matched, then return RequestedAlreadyCollaborated() exception.
- Create and save the Invitation object whose status is 'PENDING' and return this saved entity to the client.

#### 4. RejectPlayerRequest: Parameters: [Integer invitationId, Integer receiverUserProgressId, Integer eventId, Instant timestamp]

- First find the Invitation entity by invitationId and update the status of this entity from 'PENDING' to 'REJECTED'.
- Return the updated Invitation object to the client.

#### 5. AcceptPlayerRequest: Parameters: [Integer invitationId, Integer receiverUserProgressId, Integer eventId, Instant timestamp]

- Most important and complicated endpoint.
- If the requester(receiverUserProgressId) is already matched, then return RequesterAlreadyCollaborated() exception.
- If the requested(sender) is already matched, then return RequestedAlreadyCollaborated() exception.
- First find the Invitation entity by invitationId and update the status of this entity from 'PENDING' to 'APPROVED'.
- Then trigger the creation of the Collaboration entity with default values.
- Then, most importantly, update 'PENDING' invitations sent by these two users to 'INVALIDATED' so that the receivers of these invitations should not see them anymore.
- Return the updated and saved Invitation + Collaboration object to the client.
- Please inspect the returned DTO object. 

#### 6. GetBalloonsInfoRequest: Parameters: [Integer userProgressId, Integer invitationId, Instant timestamp]

- Find the Invitation entity and Collaboration entity by invitationId and return the  Invitation + Collaboration object to the client.
- The parameter "userProgressId" is used in differentiating the "requesterHeliumContribution" and "requestersFriendHeliumContribution" in the response.
- Please inspect the returned DTO object.

#### 7. UpdateBalloonProgressRequest: Parameters: [Integer userProgressId, Integer invitationId, Instant timestamp, Double consumedHelium]

- If the balloons is already burst, then return BalloonAlreadyInflated() exception.
- If the requester does not have as much helium as parameter "consumedHelium", then return InsufficientHelium() exception.
- After, edges cases and math calculations are done.(Some details here)
- If the capacity of the balloon is reached, then the status of the collaboration switches from 'IN_PROGRESS' to 'COMPLETED' and now partners have a right to claim the reward. 
- The available helium count of the requester and requester's contribution are also updated accordingly.
- Returns the updated and saved Invitation + Collaboration object to the client.
- The testGroup is respected in the implementation.
- Maybe I should return the updated userProgress information as well but no enough time. This can be added easily.

#### 8. ClaimRewardRequest: Parameters: [Integer userProgressId, Integer invitationId, Instant timestamp]

- If the balloons is not ready to burst, then return BalloonNotFullyInflated() exception.
- If the requester already has taken the reward, then return EventRewardAlreadyTaken() exception.
- After, determine whether the requester has a right to claim the reward and update accordingly.
- Update UserProgress entity as adding helium reward up to the coin.
- Save the updated entities and return the Invitation + Collaboration object to the client.
- Please inspect the control of flow of this request.
- The testGroup is respected in the implementation.

### 3.Global Leaderboard

#### 1. GetLeaderboardRequest: Parameters: [Integer userProgressId]

- "userProgressId" is used for determining the associated test group with the requester.
- Returns a flux of UserProgress.
- A hot publisher with sink for both test groups is implemented.
- In init state, leaderboard is retrieved from the database in a non-blocking way (subscribed this source internally.) and the 100th highest score for both test groups is set in an AtomicInteger.
- Then that flow of data is emitted into the sink which can store up to 100 elements in its internal queue. So that whenever a subscription is made to the Sink, this stored 100 elements are sent immediately.
- So the question is that what event should trigger the retrieval of the top-100 elements.
- I implemented the answer for this question as follows: Whenever the corresponding endpoint of the level-up() procedure
is called, the new level of the requester is made comparison with the **latest retrieved** 100th highest score based on the test group 
and if the new level is greater than the **latest retrieved** 100th highest score, then the leaderboard is retrieved from
the database and the 100th highest score for the corresponding test group is set in an AtomicInteger.

### NOTES

- In dockerfile, I changed the 5th line from "RUN mvn clean package" to "RUN mvn clean package -DskipTests". I noticed that my testcases are not fully independent but the code is tested duly.
- In docker-compose file, I added a dependency so that spring boot image starts after mysql image is ready to start accepting connections.
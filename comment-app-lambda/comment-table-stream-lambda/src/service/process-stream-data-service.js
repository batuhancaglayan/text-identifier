const { commentDaoActions } = require('../dao/comment-dao-router');

const { STREAM_EVENT_TYPES } = require('../constant');

const { generateCommentDoc } = require('../model/elasticsearch')

const { md5Hash } = require('../utils/hash-util');

const process = async ({ streamEvent }) => {

    const { 
        record,
        eventName,
    } = streamEvent;

    try {

    const action = STREAM_EVENT_TYPES[eventName];
    if (action){
        const commentAcion = commentDaoActions[action];

        const doc = generateCommentDoc(record); 
        const index = md5Hash(doc.email);

        return await commentAcion({index, doc });
    }

    return '';
            
} catch (error) {
     console.log(error)   
}

}

module.exports = {
    process
}
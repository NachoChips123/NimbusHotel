import React, { useState } from "react"
import { Button, Row } from "react-bootstrap"
import RoomCards from "./RoomCards"
import RoomPaginator from "./RoomPaginator"

const RoomSearchResults = ({ results, onClearSearch }) => {
	
    const [currentPage, setCurrentPage] = useState(1)
	const resultsPerPage = 3
	const totalResults = results.length
	const totalPages = Math.ceil(totalResults / resultsPerPage)

    const handlePageChange = (pageNumber) => {
		setCurrentPage(pageNumber)
	}

    const startIndex = (currentPage - 1) * resultsPerPage
	const endIndex = startIndex + resultsPerPage
	const paginatedResults = results.slice(startIndex, endIndex)

    return (
		<>
			{results.length > 0 ? (
				<>
					<h5 className="text-center mt-5">Search Results</h5>
					<Row>
						{paginatedResults.map((room) => (
							<RoomCards key={room.id} room={room} />
						))}
					</Row>
					<Row>
						{totalResults > resultsPerPage && (
							<RoomPaginator
								currentPage={currentPage}
								totalPages={totalPages}
								onPageChange={handlePageChange}
							/>
						)}
						<Button variant="secondary" onClick={onClearSearch} className="btn btn-hotel">
							Clear Search
						</Button>
					</Row>
				</>
			) : (
				<p></p>
			)}
		</>
	)
}
    export default RoomSearchResults
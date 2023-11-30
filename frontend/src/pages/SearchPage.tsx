import React, { useEffect, useState } from "react";
import DetailLayout from "../layout/DetailLayout";
import Detail from "../components/detail/Detail";

import styles from "./Page.module.css";
import SearchLayout from "../layout/SearchLayout";
import SearchResult from "../components/search/SearchResult";
import { useLocation } from "react-router-dom";

import image from "../asset/ddd.png";
import axios from "axios";

const SearchPage = () => {
  const { state } = useLocation();
  const [currentItems, setCurrentItems] = useState<any>([]);
  const [noResult, setNoResult] = useState<boolean>(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState<number>(state.page);
  const itemsPerPage = 10;
  const pageNumbersToShow = 5;

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;

  useEffect(() => {
    setNoResult(false);
    setCurrentItems([]);
    axios
      .get(
        `https://j8e107.p.ssafy.io/api/game/name/${state.searchInput}?page=0&size=10`
      )
      .then((res) => {
        setCurrentItems(res.data.data);
        setCurrentPage(1);
        window.scrollTo(0, 0);
        if (res.data.data.length == 0) {
          setNoResult(true);
        }
      })
      .catch((e) => {});
  }, [state.searchInput]);

  useEffect(() => {
    axios
      .get(
        `https://j8e107.p.ssafy.io/api/game/name/${state.searchInput}?page=${
          currentPage - 1
        }&size=10`
      )
      .then((res) => {
        setCurrentItems(res.data.data);
        setTotalPages(res.data.pages);
      })
      .catch((e) => {});
  }, [currentPage]);

  const handleClickPageNumber = (pageNumber: number) => {
    setCurrentPage(pageNumber);
    window.scrollTo(0, 0);
  };

  const handleGoToFirstPage = () => {
    setCurrentPage(1);
    window.scrollTo(0, 0);
  };

  const handleGoToLastPage = () => {
    setCurrentPage(totalPages);
    window.scrollTo(0, 0);
  };

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
      window.scrollTo(0, 0);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
      window.scrollTo(0, 0);
    }
  };

  const pageNumbers = [];
  if (totalPages <= pageNumbersToShow) {
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }
  } else {
    const halfPageNumbersToShow = Math.floor(pageNumbersToShow / 2);
    let startPageNumber = currentPage - halfPageNumbersToShow;
    let endPageNumber = currentPage + halfPageNumbersToShow;

    if (startPageNumber < 1) {
      endPageNumber += Math.abs(startPageNumber) + 1;
      startPageNumber = 1;
    } else if (endPageNumber > totalPages) {
      startPageNumber -= endPageNumber - totalPages;
      endPageNumber = totalPages;
    }

    for (let i = startPageNumber; i <= endPageNumber; i++) {
      pageNumbers.push(i);
    }
  }

  return (
    <SearchLayout>
      <h3 className={styles.SearchInput}>검색 : {state.searchInput}</h3>
      <div className={styles.SearchPage}>
        {currentItems && currentItems.length != 0 ? (
          <div>
            {currentItems.map((game: any, i: number) => (
              <SearchResult key={i} game={game} />
            ))}
          </div>
        ) : noResult ? (
          <div className={styles.NoResult}>
            <div>
              <img src={image} alt="" />
              <div style={{ display: "flex", justifyContent: "center" }}>
                <h2>검색 결과가 없습니다</h2>
              </div>
            </div>
          </div>
        ) : (
          <div className={styles.NoResult}>
            <h2>검색 중입니다</h2>
          </div>
        )}
        {noResult ? null : (
          <div className={styles.pageButton}>
            <button onClick={handleGoToFirstPage}>First</button>
            <button onClick={handlePrevPage}>Prev</button>
            <div className={styles.pagenation}>
              {pageNumbers.map((pageNumber) => (
                <p
                  key={pageNumber}
                  onClick={() => handleClickPageNumber(pageNumber)}
                  className={
                    currentPage === pageNumber
                      ? styles.pageActive
                      : styles.pageNumber
                  }
                >
                  {pageNumber}
                </p>
              ))}
            </div>
            <button onClick={handleNextPage}>Next</button>
            <button onClick={handleGoToLastPage}>Last</button>
          </div>
        )}
      </div>
    </SearchLayout>
  );
};

export default SearchPage;

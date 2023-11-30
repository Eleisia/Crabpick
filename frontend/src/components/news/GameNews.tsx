import React, { Component } from "react";
import axios from "axios";
import styles from "./GameNews.module.css";

interface NewsItem {
  id: number;
  subject: string;
  url: string;
  imageLink: string;
  date: string;
  sitename: string;
}

interface State {
  news: NewsItem[];
  visibleNews: number;
}

class GameNewsList extends Component<{}, State> {
  constructor(props: {}) {
    super(props);

    this.state = {
      news: [],
      visibleNews: 10,
    };
  }

  componentDidMount(): void {
    axios
      .get(`https://j8e107.p.ssafy.io/api/news`)
      .then((response) => {
        console.log(response);
        this.setState({ news: response.data });
      })
      .catch((err) => {
        console.error(err);
      });
  }

  loadMore = (): void => {
    this.setState((prevState) => ({
      visibleNews: prevState.visibleNews + 10,
    }));
  };

  render() {
    const { news, visibleNews } = this.state;

    return (
      <div style={{ minHeight: "60vh" }}>
        <h3>게임 뉴스들을 한 눈에 모아보세요</h3>
        {/* <hr /> */}
        <div className={styles.newslist}>
          <ul className={styles.newsul}>
            {news.slice(0, visibleNews).map((item) => (
              <li key={item.id} className={styles.newsli}>
                <a
                  href={item.url}
                  target="_blank"
                  rel="noreferrer"
                  className={styles.newsa}
                >
                  <div className={styles.article}>
                    <div className={styles.article_left}>
                      <img src={item.imageLink} alt="" />
                      <div style={{ marginLeft: "5%" }}>
                        <h3>{item.subject}</h3>
                      </div>
                    </div>
                    <div className={styles.article_right}>
                      <div>{item.sitename}</div>
                      <div className={styles.date}>{item.date}</div>
                    </div>
                  </div>
                </a>
              </li>
            ))}
          </ul>
          <div className={styles.morebutton}>
            {visibleNews < news.length && (
              <button onClick={this.loadMore}>더보기</button>
            )}
          </div>
        </div>
      </div>
    );
  }
}

export default GameNewsList;

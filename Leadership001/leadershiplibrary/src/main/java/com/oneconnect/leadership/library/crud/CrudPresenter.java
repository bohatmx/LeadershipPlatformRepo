package com.oneconnect.leadership.library.crud;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class CrudPresenter implements CrudContract.Presenter {
    private CrudContract.View view;
    private DataAPI dataAPI;
    private ListAPI listAPI;

    public CrudPresenter(CrudContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void addEntity(BaseDTO entity) {
          if (entity instanceof CategoryDTO) {
              CategoryDTO c = (CategoryDTO)entity;
              dataAPI.addCategory(c, new DataAPI.DataListener() {
                  @Override
                  public void onResponse(String key) {
                      view.onEntityAdded(key);
                  }

                  @Override
                  public void onError(String message) {
                      view.onError(message);
                  }
              });
          }
        if (entity instanceof CompanyDTO) {
            CompanyDTO c = (CompanyDTO)entity;
            dataAPI.addCompany(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof CountryDTO) {
            CountryDTO c = (CountryDTO)entity;
            dataAPI.addCountry(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof DeviceDTO) {
            DeviceDTO c = (DeviceDTO) entity;
            dataAPI.addDevice(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof DailyThoughtDTO) {
            DailyThoughtDTO c = (DailyThoughtDTO)entity;
            dataAPI.addDailyThought(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof EBookDTO) {
            EBookDTO c = (EBookDTO)entity;
            dataAPI.addEBook(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof NewsDTO) {
            NewsDTO c = (NewsDTO)entity;
            dataAPI.addNews(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PaymentDTO) {
            PaymentDTO c = (PaymentDTO) entity;
            dataAPI.addPayment(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PhotoDTO) {
            PhotoDTO c = (PhotoDTO) entity;
            dataAPI.addPhoto(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PodcastDTO) {
            PodcastDTO c = (PodcastDTO) entity;
            dataAPI.addPodcast(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PriceDTO) {
            PriceDTO c = (PriceDTO) entity;
            dataAPI.addPrice(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof SubscriptionDTO) {
            SubscriptionDTO c = (SubscriptionDTO) entity;
            dataAPI.addSubscription(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof UserDTO) {
            UserDTO c = (UserDTO) entity;
            dataAPI.addUser(c, new DataAPI.AddUserListener() {
                @Override
                public void onUserAdded(UserDTO user) {
                    view.onEntityAdded(user.getUserID());
                }

                @Override
                public void onUserAlreadyExists(UserDTO user) {
                     view.onEntityAdded(user.getUserID());
                }

                @Override
                public void onError(String message) {
                     view.onError(message);
                }
            });
        }
        if (entity instanceof VideoDTO) {
            VideoDTO c = (VideoDTO) entity;
            dataAPI.addVideo(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof WeeklyMasterClassDTO) {
            WeeklyMasterClassDTO c = (WeeklyMasterClassDTO) entity;
            dataAPI.addWeeklyMasterClass(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof WeeklyMessageDTO) {
            WeeklyMessageDTO c = (WeeklyMessageDTO) entity;
            dataAPI.addWeeklyMessage(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
    }

    @Override
    public void updateEntity(BaseDTO entity) {
        if (entity instanceof UserDTO) {
            UserDTO u = (UserDTO)entity;
            dataAPI.updateUser(u, new DataAPI.UpdateListener() {
                @Override
                public void onSuccess() {
                    view.onEntityUpdated();
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
    }

    @Override
    public void updateSubscription(final SubscriptionDTO subscription) {
        dataAPI.updateSubscription(subscription, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onSubscriptionUpdated(subscription);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateUser(final UserDTO user) {
        dataAPI.updateUser(user, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onUserUpdated(user);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });

    }

    @Override
    public void updateDailyThought(final DailyThoughtDTO dailyThought) {
        dataAPI.updateDailyThought(dailyThought, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onDailyThoughtUpdated(dailyThought);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateWeeklyMasterClass(final WeeklyMasterClassDTO masterClass) {
        dataAPI.updateWeeklyMasterClass(masterClass, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onWeeklyMasterClassUpdated(masterClass);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateWeeklyMessage(final WeeklyMessageDTO weeklyMessage) {
        dataAPI.updateWeeklyMessage(weeklyMessage, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onWeeklyMessageUpdated(weeklyMessage);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateNews(final NewsDTO news) {
        dataAPI.updateNews(news, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onNewsUpdated(news);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateCategory(final CategoryDTO category) {
        dataAPI.updateCategory(category, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onCategoryUpdated(category);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void createUser(UserDTO user) {
        dataAPI.createUser(user, new DataAPI.CreateUserListener() {
            @Override
            public void onUserCreated(UserDTO user) {
                view.onUserCreated(user);
            }

            @Override
            public void onUserAlreadyExists(UserDTO user) {
                view.onUserCreated(user);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteUser(final UserDTO user) {
        dataAPI.deleteUser(user, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onUserDeleted(user);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteDailyThought(final DailyThoughtDTO dailyThought) {
        dataAPI.deleteDailyThought(dailyThought, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onDailyThoughtDeleted(dailyThought);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteWeeklyMessage(final WeeklyMessageDTO weeklyMessage) {
        dataAPI.deleteWeeklyMessage(weeklyMessage, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onWeeklyMessageDeleted(weeklyMessage);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteWeeklyMasterClass(final WeeklyMasterClassDTO masterClass) {
        dataAPI.deleteWeeklyMasterClass(masterClass, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onWeeklyMasterClassDeleted(masterClass);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteVideo(final VideoDTO video) {
        dataAPI.deleteVideo(video, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onVideoDeleted(video);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deletePodcast(final PodcastDTO podcast) {
            dataAPI.deletePodcast(podcast, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onPodcastDeleted(podcast);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
    }

    @Override
    public void deleteNews(final NewsDTO news) {
        dataAPI.deleteNews(news, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onNewsDeleted(news);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deletePhoto(PhotoDTO photo) {

    }

    @Override
    public void deleteEbook(final EBookDTO eBook) {
        dataAPI.deleteBook(eBook, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onEbookDeleted(eBook);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteCategory(final CategoryDTO category) {
        dataAPI.deleteCategory(category, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onCategoryDeleted(category);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteSubscription(final SubscriptionDTO subscription) {
        dataAPI.deleteSubscription(subscription, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onSubscriptionDeleted(subscription);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCategories(String companyID) {
         listAPI.getCategories(companyID, new ListAPI.DataListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onCategories(bag.getCategories());
             }

             @Override
             public void onError(String messsage) {
                 view.onError(messsage);
             }
         });
    }

    @Override
    public void getAllSubscriptions() {
        listAPI.getAllSubscriptions(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onSubscriptions(bag.getSubscriptions());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getCompanies(String companyID) {
       listAPI.getCompanies(new ListAPI.DataListener() {
           @Override
           public void onResponse(ResponseBag bag) {
               view.onCompanies(bag.getCompanies());
           }

           @Override
           public void onError(String messsage) {
              view.onError(messsage);
           }
       });
    }

    @Override
    public void getDailyThoughts(String companyID) {
        listAPI.getDailyThoughts(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onDailyThoughts(bag.getDailyThoughts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getEbooks(String companyID) {
         listAPI.getEBooks(companyID, new ListAPI.DataListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onEbooks(bag.geteBooks());
             }

             @Override
             public void onError(String messsage) {
                 view.onError(messsage);
             }
         });
    }

    @Override
    public void getPayments(String companyID) {
        listAPI.getPayments(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onPayments(bag.getPayments());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getPodcasts(String companyID) {
       listAPI.getPodcasts(companyID, new ListAPI.DataListener() {
           @Override
           public void onResponse(ResponseBag bag) {
               view.onPodcasts(bag.getPodcasts());
           }

           @Override
           public void onError(String messsage) {
               view.onError(messsage);
           }
       });
    }

    @Override
    public void getPhotos(String companyID) {
         listAPI.getPhotos(companyID, new ListAPI.DataListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onPhotos(bag.getPhotos());
             }

             @Override
             public void onError(String messsage) {
                 view.onError(messsage);
             }
         });
    }

    @Override
    public void getPrices(String companyID) {
           listAPI.getPrices(companyID, new ListAPI.DataListener() {
               @Override
               public void onResponse(ResponseBag bag) {
                   view.onPrices(bag.getPrices());
               }

               @Override
               public void onError(String messsage) {
                   view.onError(messsage);
               }
           });
    }

    @Override
    public void getUsers(String companyID) {
          listAPI.getCompanyStaff(companyID, new ListAPI.DataListener() {
              @Override
              public void onResponse(ResponseBag bag) {
                  view.onUsers(bag.getUsers());
              }

              @Override
              public void onError(String messsage) {
                  view.onError(messsage);
              }
          });
    }

    @Override
    public void getUser(String email) {
        dataAPI.getUserByEmail(email, new DataAPI.EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {
                view.onUserFound(user);
            }

            @Override
            public void onUserNotFoundByEmail() {

            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getNews(String companyID) {
         listAPI.getNews(companyID, new ListAPI.DataListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onNews(bag.getNews());
             }

             @Override
             public void onError(String messsage) {
                 view.onError(messsage);
             }
         });
    }

    @Override
    public void getSubscriptions(String companyID) {
        listAPI.getSubscriptions(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onSubscriptions(bag.getSubscriptions());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getVideos(String companyID) {
         listAPI.getCompanyVideos(companyID, new ListAPI.DataListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onVideos(bag.getVideos());
             }

             @Override
             public void onError(String messsage) {
                 view.onError(messsage);
             }
         });
    }

    @Override
    public void getWeeklyMasterclasses(String companyID) {
          listAPI.getWeeklyMasterclasses(companyID, new ListAPI.DataListener() {
              @Override
              public void onResponse(ResponseBag bag) {
                  view.onWeeklyMasterclasses(bag.getWeeklyMasterClasses());
              }

              @Override
              public void onError(String messsage) {
                  view.onError(messsage);
              }
          });
    }

    @Override
    public void getWeeklyMessages(String companyID) {
         listAPI.getWeeklyMessages(companyID, new ListAPI.DataListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onWeeklyMessages(bag.getWeeklyMessages());
             }

             @Override
             public void onError(String messsage) {
                 view.onError(messsage);
             }
         });
    }

    @Override
    public void getDevices(String companyID) {
        listAPI.getDevices(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onDevices(bag.getDevices());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }
}

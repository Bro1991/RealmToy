package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.event.CreateLibraryEvent;
import com.memolease.realmtoy.event.DeleteLibraryEvent;
import com.memolease.realmtoy.event.EditLibraryEvent;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.Library;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by bro on 2017-10-13.
 */

public class LibraryAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    List<Library> items;
    LayoutInflater inflater;
    Context context;
    Bus mBus = BusProvider.getInstance();
    Realm realm;

    public Boolean editMode = false;

    Library wantto;
    Library before;
    Library now;
    Library after;

    public LibraryAdapter(Context context, List<Library> libraryList, Realm realm) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = libraryList;

        this.wantto = new Library();
        this.wantto.setTitle("구입희망");
        this.wantto.setType(1);
        this.wantto.setRead_state(1);

        this.before = new Library();
        this.before.setTitle("읽을 예정");
        this.before.setType(1);
        this.before.setRead_state(2);

        this.now = new Library();
        this.now.setTitle("읽는 중");
        this.now.setType(1);
        this.now.setRead_state(3);

        this.after = new Library();
        this.after.setTitle("다 읽음");
        this.after.setType(1);
        this.after.setRead_state(4);

        this.realm = realm;
    }

    public void updateStateLibraryCount(HashMap<String, Integer> count) {
        wantto.setBookCount(count.get("wantto"));
        before.setBookCount(count.get("before"));
        now.setBookCount(count.get("now"));
        after.setBookCount(count.get("after"));

        notifyDataSetChanged();
    }

    @Override
    public long getHeaderId(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Library getItem(int position) {
        return items.get(position);
    }

    public List<Library> getItems() {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean toggleLibraryBtn(boolean flag) {

//        if (flag) {
//            for (Button btn : editBtns) {
//                btn.setVisibility(View.VISIBLE);
//            }
//
//            for (Button btn : deleteBtns) {
//                btn.setVisibility(View.VISIBLE);
//            }
//        } else {
//            for (Button btn : editBtns) {
//                btn.setVisibility(View.GONE);
//            }
//
//            for (Button btn : deleteBtns) {
//                btn.setVisibility(View.GONE);
//            }
//        }
        return !flag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.d("getVIew", "getview 시작");

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.library_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.title);
            holder.text.setTextColor(Color.parseColor("#000000"));

            if (items.get(position).getType() == 0) {
                if (items.get(position).getLibType() == 0) {
                    holder.editBtn = (Button) convertView.findViewById(R.id.editLibraryListButton);
                    holder.deleteBtn = (Button) convertView.findViewById(R.id.deleteLibraryListButton);
                } else {
                    holder.layout = (RelativeLayout) convertView.findViewById(R.id.libraryListLayout);
                }
            }
            Log.d("getVIew", "getview 뷰 생성");
            convertView.setTag(holder);
        } else {
            Log.d("getVIew", "getview 뷰 이미 있음");
            holder = (ViewHolder) convertView.getTag();
        }
        Log.d("getVIew", "getview 시작");
        final Library thisLibrary = items.get(position);

        if (items.get(position).getLibType() == 1) {
            RealmResults<Book> bookRealmResults = realm.where(Book.class).equalTo("libraryid", thisLibrary.getId()).findAll();
            Log.d("디비에서 가져온 갯수", String.valueOf(bookRealmResults.size()));
            holder.text.setText(thisLibrary.getTitle()+"("+bookRealmResults.size()+")");
        } else {
            RealmResults<Book> readstateRealmResults = realm.where(Book.class).equalTo("readState", thisLibrary.getRead_state()).findAll();
            holder.text.setText(thisLibrary.getTitle()+"("+readstateRealmResults.size()+")");
        }

/*        if (bookRealmResults.size() != 0) {
            holder.text.setText(thisLibrary.getTitle()+"("+bookRealmResults.size()+")");
        } else {
            holder.text.setText(thisLibrary.getTitle()+"("+ 0 +")");
        }*/






        //holder.text.setText(thisLibrary.getTitle()+"("+thisLibrary.getBookCount()+")");

        if (thisLibrary.getType() == 0) {
            if (thisLibrary.getLibType() == 0) {
                if (holder.editBtn != null && holder.deleteBtn != null) {
                    holder.editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialDialog.Builder(context)
                                    .title("책장 이름")
                                    .content("책장 이름을 입력해주세요")
                                    .inputType(InputType.TYPE_CLASS_TEXT)
                                    .input("책장 이름", thisLibrary.getTitle(), new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(MaterialDialog dialog, CharSequence input) {
                                        }
                                    })
                                    .positiveText("추가")
                                    .negativeText("취소")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            String title = dialog.getInputEditText().getText().toString();

                                            EditLibraryEvent event = new EditLibraryEvent();
                                            event.setId(thisLibrary.getId());
                                            event.setTitle(title);
                                            mBus.post(event);
                                        }
                                    })
                                    .show();
                        }
                    });

                    holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialDialog.Builder(context)
                                    .content("삭제되는 책장의 책들은 기본 책장으로 이동됩니다")
                                    .positiveText("삭제")
                                    .negativeText("취소")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            DeleteLibraryEvent event = new DeleteLibraryEvent();
                                            event.setId(thisLibrary.getId());
                                            mBus.post(event);
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            } else {
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editMode) {
/*                            if (thisLibrary.getType() == 1)
                                UserSharedPreference.getInstance().setSelectedLibraryState(thisLibrary.getRead_state());
                            else
                                UserSharedPreference.getInstance().setSelectedLibrary(thisLibrary.getId());

                            FetchBooksEvent event = new FetchBooksEvent();
                            event.setUserId(UserSharedPreference.getInstance().getId());
                            mBus.post(event);

                            CloseDrawerEvent _event = new CloseDrawerEvent();
                            mBus.post(_event);*/
                        }
                    }
                });
            }

        }

        if (holder.editBtn != null && holder.deleteBtn != null) {
            if (editMode && thisLibrary.getType() == 0 && thisLibrary.getLibType() == 0) {
                holder.editBtn.setVisibility(View.VISIBLE);
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.editBtn.setVisibility(View.GONE);
                holder.deleteBtn.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.library_listview_header, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.title);
            holder.textView.setTextColor(Color.parseColor("#ffffff"));

            holder.addBtn = (Button) convertView.findViewById(R.id.addLibraryButton);
            holder.editBtn = (Button) convertView.findViewById(R.id.editLibraryButton);
            holder.editBtn.setTextColor(Color.parseColor("#ffffff"));

            if (editMode) {
                holder.editBtn.setText("완료");
            } else {
                holder.editBtn.setText("편집");
            }
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        if (getHeaderId(position) == 0) {
            holder.textView.setText("내 책장");
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editMode) {
                        ((Button) v).setText("편집");
                    } else {
                        ((Button) v).setText("완료");
                    }

                    editMode = toggleLibraryBtn(editMode);
                    notifyDataSetChanged();
                }
            });

            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("책장 이름")
                            .content("책장 이름을 입력해주세요")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input("책장 이름", null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                }
                            })
                            .positiveText("추가")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    String title = dialog.getInputEditText().getText().toString();
                                    CreateLibraryEvent event = new CreateLibraryEvent();
                                    event.setTitle(title);
                                    event.setLibType(0);
                                    mBus.post(event);
                                }
                            })
                            .show();
                }
            });
        } else {
            holder.textView.setText("독서 상태 목록");
            holder.addBtn.setVisibility(View.GONE);
            holder.editBtn.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView text;
        Button editBtn;
        Button deleteBtn;

        RelativeLayout layout;
    }

    class HeaderViewHolder {
        TextView textView;
        Button addBtn;
        Button editBtn;
    }

    public void refresh() {
        if (items == null)
            items = new ArrayList<>();
        else
            items.clear();
    }

    public void addrealmItems(RealmResults<Library> newItems) {
        for (Library item : newItems)
            items.add(item);
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}


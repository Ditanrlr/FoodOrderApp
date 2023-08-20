package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.foodorderapp.adapters.PlaceYourOrderAdapter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.RestaurantModel;

public class PlaceYourOrderActivity extends AppCompatActivity {

    private EditText inputName, inputAddress,inputNohp;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_your_order);

        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurantModel.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputName = findViewById(R.id.inputName);
        inputAddress = findViewById(R.id.inputAddress);
        inputNohp = findViewById(R.id.inputNohp);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);
        switchDelivery = findViewById(R.id.switchDelivery);
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(restaurantModel);
            }
        });

        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    inputAddress.setVisibility(View.VISIBLE);

                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    isDeliveryOn = true;
                    calculateTotalAmount(restaurantModel);
                } else {
                    inputAddress.setVisibility(View.GONE);

                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    isDeliveryOn = false;
                    calculateTotalAmount(restaurantModel);
                }
            }
        });
        initRecyclerView(restaurantModel);
        calculateTotalAmount(restaurantModel);
    }

    private void calculateTotalAmount(RestaurantModel restaurantModel) {
        float subTotalAmount = 0f;

        for(Menu m : restaurantModel.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        tvSubtotalAmount.setText("Rp."+String.format("%.3f", subTotalAmount));
        if(isDeliveryOn) {
            tvDeliveryChargeAmount.setText("Rp."+String.format("%.3f", restaurantModel.getDelivery_charge()));
            subTotalAmount += restaurantModel.getDelivery_charge();
        }
        tvTotalAmount.setText("Rp."+String.format("%.3f", subTotalAmount));
    }

    private void onPlaceOrderButtonClick(RestaurantModel restaurantModel) {
        if(TextUtils.isEmpty(inputName.getText().toString())) {
            inputName.setError("Please enter name ");
            return;
        } else if(isDeliveryOn && TextUtils.isEmpty(inputAddress.getText().toString())) {
            inputAddress.setError("Please enter address ");
            return;
        } else if(isDeliveryOn && TextUtils.isEmpty(inputNohp.getText().toString())) {
            inputNohp.setError("Please enter No Handphone ");
            return;

        }
        //start success activity..
        Intent i = new Intent(PlaceYourOrderActivity.this, OrderSucceessActivity.class);
        i.putExtra("RestaurantModel", restaurantModel);
        startActivityForResult(i, 1000);
    }

    private void initRecyclerView(RestaurantModel restaurantModel) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(restaurantModel.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}